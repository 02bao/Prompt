Frontend tĩnh (HTML/CSS) cho trang login.

Deploy lên Vercel:
- Kéo folder `frontend` lên GitHub.
- Trên Vercel, tạo project mới, kết nối repo.
- Build settings: Framework - Other, Build Command - (leave empty), Output Directory - (root)
- Cập nhật `API_BASE` environment variable trong Vercel settings trỏ tới backend Render URL.
