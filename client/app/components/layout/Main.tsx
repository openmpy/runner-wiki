export default function Main({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex-1 p-6 bg-white border-t border-b md:border border-[#00A495] rounded-none md:rounded-lg">
      {children}
    </div>
  );
}
