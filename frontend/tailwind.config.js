/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {},
  },
  plugins: [],
  safelist: ["ribbon", "ribbon.ordered"],
  variants: {
    extend: {
      translate: ["responsive"],
    },
  },
};
