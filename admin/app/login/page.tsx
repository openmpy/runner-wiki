import Button from "@/components/ui/Button";

export default function LoginPage() {
  return (
    <div>
      <h1 className="font-bm-hanna text-2xl mb-6">로그인</h1>

      <div className="space-y-4">
        <div>
          <label
            htmlFor="username"
            className="block text-sm font-medium text-gray-700 mb-2 font-bm-hanna"
          >
            아이디
          </label>
          <input
            id="username"
            type="text"
            placeholder="아이디를 입력해주세요."
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none"
          />
        </div>

        <div>
          <label
            htmlFor="password"
            className="block text-sm font-medium text-gray-700 mb-2 font-bm-hanna"
          >
            패스워드
          </label>
          <input
            id="password"
            type="password"
            placeholder="패스워드를 입력해주세요."
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none"
          />
        </div>

        <div className="pt-4">
          <Button type="submit" className="w-full">
            로그인
          </Button>
        </div>
      </div>
    </div>
  );
}
