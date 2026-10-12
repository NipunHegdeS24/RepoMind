import type { SVGProps } from "react";

import { cn } from "@/lib/utils";

type RepoMindIconProps = SVGProps<SVGSVGElement> & {
  variant?: "color" | "mono";
};

function RepoMindMark({ mono = false }: { mono?: boolean }) {
  const foreground = mono ? "var(--background)" : "#FFFFFF";
  const accent = mono ? "var(--background)" : "#FDE68A";

  return (
    <>
      <rect
        width="64"
        height="64"
        rx="15"
        fill={mono ? "currentColor" : "#0D9488"}
      />
      <path
        d="M32 20v9m0 0H20v11m12-11h12v11M32 29v11"
        stroke={foreground}
        strokeWidth="3"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
      <circle cx="32" cy="16" r="4" fill={foreground} />
      <circle cx="20" cy="44" r="4" fill={foreground} />
      <circle cx="32" cy="44" r="4" fill={foreground} />
      <circle cx="44" cy="44" r="4" fill={foreground} />
      <path
        d="m48 10 1.4 3.6L53 15l-3.6 1.4L48 20l-1.4-3.6L43 15l3.6-1.4L48 10Z"
        fill={accent}
      />
    </>
  );
}

export function RepoMindIcon({
  className,
  variant = "color",
  ...props
}: RepoMindIconProps) {
  return (
    <svg
      viewBox="0 0 64 64"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
      className={cn("shrink-0", className)}
      {...props}
    >
      <RepoMindMark mono={variant === "mono"} />
    </svg>
  );
}

export function RepoMindLogo({
  className,
  ...props
}: SVGProps<SVGSVGElement>) {
  return (
    <svg
      viewBox="0 0 238 64"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
      className={cn("shrink-0", className)}
      {...props}
    >
      <RepoMindMark />
      <text
        x="80"
        y="43"
        fill="currentColor"
        fontFamily="Arial, sans-serif"
        fontSize="30"
        fontWeight="600"
        letterSpacing="-1.2"
      >
        RepoMind
      </text>
    </svg>
  );
}
