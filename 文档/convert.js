const fs = require('fs');
const path = require('path');
const { Document, Packer, Paragraph, HeadingLevel, Table, TableRow, TableCell, TextRun, VerticalAlign, WidthType } = require('docx');

const docsDir = __dirname;
const docFiles = [
  '李战奇-个人简历.md'
];

function parseMarkdownToDocx(mdContent) {
  const paragraphs = [];
  const lines = mdContent.split('\n');
  
  let i = 0;
  while (i < lines.length) {
    const line = lines[i];
    
    if (line.startsWith('# ')) {
      paragraphs.push(new Paragraph({
        heading: HeadingLevel.HEADING_1,
        children: [new TextRun({ text: line.replace('# ', ''), bold: true, size: 32 })]
      }));
      i++;
    } else if (line.startsWith('## ')) {
      paragraphs.push(new Paragraph({
        heading: HeadingLevel.HEADING_2,
        children: [new TextRun({ text: line.replace('## ', ''), bold: true, size: 28 })]
      }));
      i++;
    } else if (line.startsWith('### ')) {
      paragraphs.push(new Paragraph({
        heading: HeadingLevel.HEADING_3,
        children: [new TextRun({ text: line.replace('### ', ''), bold: true, size: 24 })]
      }));
      i++;
    } else if (line.startsWith('#### ')) {
      paragraphs.push(new Paragraph({
        heading: HeadingLevel.HEADING_4,
        children: [new TextRun({ text: line.replace('#### ', ''), bold: true, size: 22 })]
      }));
      i++;
    } else if (line.startsWith('|')) {
      const tableData = [];
      while (i < lines.length && lines[i].startsWith('|')) {
        const cells = lines[i].split('|').map(c => c.trim()).filter(c => c.length > 0);
        if (cells.length > 0) {
          tableData.push(cells);
        }
        i++;
      }
      
      if (tableData.length >= 2) {
        const rows = [];
        tableData.forEach((row, rowIndex) => {
          const cells = row.map(cell => {
            return new TableCell({
              children: [new Paragraph({
                children: [new TextRun({ text: cell, bold: rowIndex === 0, size: 20 })]
              })],
              verticalAlign: VerticalAlign.CENTER
            });
          });
          rows.push(new TableRow({ children: cells }));
        });
        
        paragraphs.push(new Table({
          rows,
          width: { size: 9000, type: WidthType.DXA },
          margins: { top: 100, bottom: 100, left: 50, right: 50 }
        }));
      }
    } else if (line.startsWith('- ') || line.startsWith('* ')) {
      paragraphs.push(new Paragraph({
        children: [new TextRun({ text: `• ${line.substring(2)}`, size: 20 })],
        indent: { left: 420, hanging: 420 }
      }));
      i++;
    } else if (line.startsWith('```')) {
      let codeContent = '';
      i++;
      while (i < lines.length && !lines[i].startsWith('```')) {
        codeContent += lines[i] + '\n';
        i++;
      }
      i++;
      paragraphs.push(new Paragraph({
        children: [new TextRun({ text: codeContent.trim(), font: 'Consolas', size: 18, color: '2E7D32' })],
        spacing: { before: 200, after: 200 }
      }));
    } else if (line.startsWith('>')) {
      paragraphs.push(new Paragraph({
        children: [new TextRun({ text: line.replace('> ', ''), italic: true, size: 20, color: '546E7A' })],
        indent: { left: 420 }
      }));
      i++;
    } else if (line.startsWith('**') && line.endsWith('**')) {
      paragraphs.push(new Paragraph({
        children: [new TextRun({ text: line.replace(/\*\*/g, ''), bold: true, size: 20 })]
      }));
      i++;
    } else if (line.trim().length > 0) {
      paragraphs.push(new Paragraph({
        children: [new TextRun({ text: line.trim(), size: 20 })],
        spacing: { before: 100, after: 100 }
      }));
      i++;
    } else {
      paragraphs.push(new Paragraph({ spacing: { after: 200 } }));
      i++;
    }
  }
  
  return paragraphs;
}

async function convertMdToDocx(mdFile) {
  const mdPath = path.join(docsDir, mdFile);
  const mdContent = fs.readFileSync(mdPath, 'utf-8');
  
  const paragraphs = parseMarkdownToDocx(mdContent);
  
  const doc = new Document({
    sections: [{
      properties: {
        page: {
          margin: { top: 1440, right: 1440, bottom: 1440, left: 1440 },
          size: { width: 11906, height: 16838 }
        },
        header: {
          children: [new Paragraph({
            children: [new TextRun({ text: '商丘市农业农村局智能农田管理系统', bold: true, size: 24 })],
            alignment: 'center'
          })]
        }
      },
      children: paragraphs
    }]
  });
  
  const buffer = await Packer.toBuffer(doc);
  const docxPath = mdPath.replace('.md', '.docx');
  fs.writeFileSync(docxPath, buffer);
  
  console.log(`✓ ${mdFile} → ${path.basename(docxPath)}`);
}

async function main() {
  console.log('开始转换Markdown文档为Word格式...\n');
  
  for (const file of docFiles) {
    await convertMdToDocx(file);
  }
  
  console.log('\n转换完成！所有文档已保存为.docx格式。');
}

main().catch(err => {
  console.error('转换失败:', err);
  process.exit(1);
});
