import { Geist } from "next/font/google";
import localFont from "next/font/local";

export const geist = Geist({
  subsets: ["latin"],
  display: "swap",
  variable: "--font-geist",
});

export const bmhanna = localFont({
  src: "../public/fonts/BMHANNA_11yrs_otf.otf",
  display: "swap",
  variable: "--font-bmhanna",
});
