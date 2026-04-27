from app.core.models import Language
from app.feedback.generator import pick_feedback


def test_feedback_combines_top_two_issues() -> None:
    text = pick_feedback(Language.en, ["shallow_depth", "excessive_forward_lean"])
    assert "deeper" in text.lower()
    assert "chest" in text.lower()


def test_feedback_unifies_knee_valgus_sides() -> None:
    text = pick_feedback(Language.en, ["knee_valgus_left", "knee_valgus_right"])
    assert "knees" in text.lower()
    assert "toes" in text.lower()


def test_feedback_falls_back_to_positive() -> None:
    text = pick_feedback(Language.en, [])
    assert "good rep" in text.lower()
