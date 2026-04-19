import { Outlet } from "react-router-dom";
import { AiCoachProvider } from "../../context/aiCoachContext.jsx";
import t from "./aiCoachTheme.module.css";

export default function AiCoachLayout() {
  return (
    <AiCoachProvider>
      <div className={`${t.root} ${t.fadeIn}`}>
        <Outlet />
      </div>
    </AiCoachProvider>
  );
}
