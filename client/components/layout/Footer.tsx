export default function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer
      style={{ backgroundColor: "#00A495" }}
      className="text-white py-3 mt-auto"
    >
      <div className="container mx-auto px-4">
        <div className="text-center">
          <p className="text-sm">{currentYear} 레디스. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}
