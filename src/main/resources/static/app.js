// ============================================================
//  FitnessTacker — Shared API Layer & Utilities
//  Change API_BASE to match your running backend:
//    Local dev  → http://localhost:8080
//    Docker     → http://localhost:8085
// ============================================================

const API_BASE = "";

// ── Auth helpers ────────────────────────────────────────────
const Auth = {
  set(token, user) {
    localStorage.setItem("ft_token", token);
    localStorage.setItem("ft_user", JSON.stringify(user));
  },
  clear() {
    localStorage.removeItem("ft_token");
    localStorage.removeItem("ft_user");
  },
  token() {
    return localStorage.getItem("ft_token");
  },
  user() {
    try {
      return JSON.parse(localStorage.getItem("ft_user")) || null;
    } catch {
      return null;
    }
  },
  userId() {
    return Auth.user()?.id || null;
  },
  isLoggedIn() {
    return !!Auth.token() && !!Auth.userId();
  },
  require() {
    if (!Auth.isLoggedIn()) {
      window.location.href = "index.html";
      return false;
    }
    return true;
  },
  logout() {
    Auth.clear();
    window.location.href = "index.html";
  },
};

// ── Core fetch wrapper ───────────────────────────────────────
async function api(path, method = "GET", body = null, extraHeaders = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...extraHeaders,
  };
  if (Auth.token()) {
    headers["Authorization"] = `Bearer ${Auth.token()}`;
  }

  const options = { method, headers };
  if (body) options.body = JSON.stringify(body);

  const res = await fetch(`${API_BASE}${path}`, options);

  // Handle empty responses (e.g. 401 with no body)
  let data = null;
  const text = await res.text();
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!res.ok) {
    // data may be a Spring validation error map or a string
    const msg =
      typeof data === "object" && data !== null
        ? Object.values(data).join(" | ")
        : data || `HTTP ${res.status}`;
    throw new Error(msg);
  }
  return data;
}

// ── Auth API ─────────────────────────────────────────────────
const AuthAPI = {
  register: (payload) => api("/api/auth/register", "POST", payload),
  login: (payload) => api("/api/auth/login", "POST", payload),
};

// ── Activity API ─────────────────────────────────────────────
const ActivityAPI = {
  /**
   * POST /api/activities
   * body: { userId, type, duration, caloriesBurned, startTime, additionalMetrics }
   */
  track: (payload) => api("/api/activities", "POST", payload),

  /**
   * GET /api/activities
   * header: X-USER-ID
   */
  getByUser: (userId) =>
    api("/api/activities", "GET", null, { "X-USER-ID": userId }),
};

// ── Recommendation API ────────────────────────────────────────
const RecommendationAPI = {
  /**
   * POST /api/recommendation/generate
   * body: { userId, activityId, improvements[], suggestions[], safety[] }
   */
  generate: (payload) => api("/api/recommendation/generate", "POST", payload),

  /** GET /api/recommendation/user/{userId} */
  getByUser: (userId) => api(`/api/recommendation/user/${userId}`),

  /** GET /api/recommendation/activity/{activityId} */
  getByActivity: (activityId) =>
    api(`/api/recommendation/activity/${activityId}`),
};

const UserAPI = {
  getAll: () => api("/api/users"),
};


// ── UI Utilities ─────────────────────────────────────────────
const UI = {
  /** Show a toast notification */
  toast(message, type = "success") {
    const existing = document.querySelector(".ft-toast");
    if (existing) existing.remove();

    const el = document.createElement("div");
    el.className = `ft-toast ft-toast--${type}`;
    el.innerHTML = `
      <span class="ft-toast__icon">${type === "success" ? "✓" : "✕"}</span>
      <span>${message}</span>
    `;
    document.body.appendChild(el);
    requestAnimationFrame(() => el.classList.add("ft-toast--show"));
    setTimeout(() => {
      el.classList.remove("ft-toast--show");
      setTimeout(() => el.remove(), 300);
    }, 3500);
  },

  /** Set a button into loading state */
  btnLoading(btn, loading = true) {
    if (loading) {
      btn.dataset.originalText = btn.textContent;
      btn.disabled = true;
      btn.classList.add("btn--loading");
      btn.textContent = "Loading…";
    } else {
      btn.disabled = false;
      btn.classList.remove("btn--loading");
      btn.textContent = btn.dataset.originalText;
    }
  },

  /** Format a LocalDateTime string for display */
  formatDate(dateStr) {
    if (!dateStr) return "—";
    return new Date(dateStr).toLocaleString(undefined, {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  },

  /** Format duration in minutes */
  formatDuration(mins) {
    if (!mins) return "—";
    if (mins < 60) return `${mins}m`;
    return `${Math.floor(mins / 60)}h ${mins % 60}m`;
  },

  /** Activity type icons */
  activityIcon(type) {
    const icons = {
      WALKING: "🚶",
      RUNNING: "🏃",
      CYCLING: "🚴",
      SWIMMING: "🏊",
      WEIGHT_TRAINING: "🏋️",
      YOGA: "🧘",
      CARDIO: "❤️",
      STRETCHING: "🤸",
      HIIT: "⚡",
      OTHERS: "🏅",
    };
    return icons[type] || "🏅";
  },

  /** Render the top navbar with logout */
  renderNav(activePage) {
    const nav = document.getElementById("nav");
    if (!nav) return;
    const user = Auth.user();
    const pages = [
      { href: "dashboard.html", label: "Dashboard" },
      { href: "activities.html", label: "Activities" },
      { href: "community.html", label: "Community" },
      { href: "recommendations.html", label: "Recommendations" },
    ];
    nav.innerHTML = `
      <div class="nav__brand">
        <span class="nav__logo">FT</span>
        <span class="nav__title">FitnessTracker</span>
      </div>
      <button class="nav__hamburger" onclick="document.querySelector('.nav__links').classList.toggle('active'); document.querySelector('.nav__user').classList.toggle('active');">
        <span></span>
        <span></span>
        <span></span>
      </button>
      <ul class="nav__links">
        ${pages
        .map(
          (p) =>
            `<li><a href="${p.href}" class="${p.href === activePage ? "active" : ""}">${p.label}</a></li>`
        )
        .join("")}
      </ul>
      <div class="nav__user">
        <span class="nav__avatar">${user?.firstName?.[0]?.toUpperCase() || "U"}</span>
        <span class="nav__name">${user?.firstName || "User"}</span>
        <button class="btn btn--ghost btn--sm" onclick="Auth.logout()">Logout</button>
      </div>
    `;
  },
};

// ── Activity Types (matches Java enum) ───────────────────────
const ACTIVITY_TYPES = [
  "WALKING",
  "RUNNING",
  "CYCLING",
  "SWIMMING",
  "WEIGHT_TRAINING",
  "YOGA",
  "CARDIO",
  "STRETCHING",
  "HIIT",
  "OTHERS",
];
