interface DocumentTitleProps {
  text: string;
}

export default function DocumentTitle({ text }: DocumentTitleProps) {
  return <h1 className="font-bmhanna text-xl lg:text-2xl mb-4">{text}</h1>;
}
