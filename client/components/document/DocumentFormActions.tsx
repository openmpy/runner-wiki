"use client";

import { useCallback, useEffect, useRef, useState } from "react";

interface DocumentFormActionsProps {
  onSubmit: () => void;
  onCancel: () => void;
  isSubmitting: boolean;
  disabled?: boolean;
  onTokenChange: (token: string) => void;
}

export default function DocumentFormActions({
  onSubmit,
  onCancel,
  isSubmitting,
  disabled = false,
  onTokenChange,
}: DocumentFormActionsProps) {
  const ref = useRef<HTMLDivElement>(null);
  const widgetIdRef = useRef<string | null>(null);

  const [verified, setVerified] = useState(false);

  const handleVerify = useCallback(
    (token: string) => {
      setVerified(true);
      onTokenChange(token);
    },
    [onTokenChange]
  );

  const handleInvalid = useCallback(() => {
    setVerified(false);
    onTokenChange("");
  }, [onTokenChange]);

  useEffect(() => {
    let cancelled = false;

    const tryRender = () => {
      const turnstile = (window as any).turnstile;
      if (cancelled) return;
      if (!turnstile) return false;
      if (!ref.current) return false;

      if (widgetIdRef.current) {
        try {
          turnstile.remove(widgetIdRef.current);
        } catch {}
        widgetIdRef.current = null;
      }

      widgetIdRef.current = turnstile.render(ref.current, {
        sitekey: process.env.NEXT_PUBLIC_TURNSTILE_SITE_KEY,
        callback: handleVerify,
        "error-callback": handleInvalid,
        "expired-callback": handleInvalid,
      });

      return true;
    };

    if (!tryRender()) {
      const timer = window.setInterval(() => {
        if (tryRender()) window.clearInterval(timer);
      }, 50);

      return () => {
        cancelled = true;
        window.clearInterval(timer);
        const turnstile = (window as any).turnstile;
        if (turnstile && widgetIdRef.current) {
          try {
            turnstile.remove(widgetIdRef.current);
          } catch {}
        }
      };
    }

    return () => {
      cancelled = true;
      const turnstile = (window as any).turnstile;
      if (turnstile && widgetIdRef.current) {
        try {
          turnstile.remove(widgetIdRef.current);
        } catch {}
      }
    };
  }, [handleVerify, handleInvalid]);

  const isDisabled = isSubmitting || disabled || !verified;

  return (
    <div>
      <div className="flex justify-end">
        <div ref={ref} className="mb-3" />
      </div>

      <div className="flex justify-end gap-2">
        <button
          className="bg-gray-400 dark:bg-zinc-500 font-bmhanna text-white dark:text-zinc-200 rounded-sm hover:opacity-90 transition-opacity px-4 py-1 cursor-pointer"
          onClick={onCancel}
          type="button"
        >
          뒤로가기
        </button>

        <button
          onClick={onSubmit}
          disabled={isDisabled}
          type="button"
          className={`bg-mint dark:bg-zinc-700 font-bmhanna text-white dark:text-zinc-200 rounded-sm px-4 py-1
            ${
              isDisabled
                ? "opacity-50 cursor-not-allowed"
                : "hover:opacity-90 cursor-pointer"
            }`}
        >
          제출하기
        </button>
      </div>
    </div>
  );
}
