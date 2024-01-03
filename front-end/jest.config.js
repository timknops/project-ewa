module.exports = {
  preset: '@vue/cli-plugin-unit-jest',
  setupFilesAfterEnv: [
    "jest-expect-message"
  ],
  testMatch: [
    "**/src/**/*.spec.js",
    "**/tests/unit/**/*.spec.js"
  ],
  moduleNameMapper: {
    "^@/(.*)$": "<rootDir>/src/$1",
    "/images/(.*)$": "<rootDir>/public/images/$1",
    "\\.(css|less|scss|sass)$": "identity-obj-proxy"
  },
  transform: {
    "^.+\\.js$": "babel-jest",
    "^.+\\.vue$": "@vue/vue3-jest",
    "^.+\\.(jpg|jpeg|png|gif|svg|mp3|mp4|mpeg)$": "<rootDir>/jest-media-transformer.js"
  },
  verbose: false
}
