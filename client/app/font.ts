import { Noto_Sans_KR } from "next/font/google";
import localFont from "next/font/local";

export const notoSans = Noto_Sans_KR({
  display: "swap",
  variable: "--font-noto-sans",
  weight: "400",
});

export const bmhanna = localFont({
  src: "../public/fonts/BMHANNA_11yrs_otf.otf",
  display: "swap",
  variable: "--font-bmhanna",
  preload: false,
});
