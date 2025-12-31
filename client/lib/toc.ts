import GithubSlugger from "github-slugger";

export type TocItem = {
  depth: number;
  text: string;
  id: string;
};

export function getTocFromMarkdown(markdown: string): TocItem[] {
  const slugger = new GithubSlugger();
  const lines = markdown.split("\n");

  const toc: TocItem[] = [];
  let inCodeBlock = false;

  for (const line of lines) {
    if (/^\s*```/.test(line)) {
      inCodeBlock = !inCodeBlock;
      continue;
    }
    if (inCodeBlock) continue;

    const match = /^(#{1,6})\s+(.+?)\s*$/.exec(line);
    if (!match) continue;

    const depth = match[1].length;
    const rawText = match[2].trim();

    const text = rawText.replace(/\s+#+\s*$/, "").trim();
    if (!text) continue;

    const id = slugger.slug(text);
    toc.push({ depth, text, id });
  }
  return toc;
}
