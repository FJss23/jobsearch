import { InputHTMLAttributes } from "react";
import styles from "./Input.module.css";

function Input({
  label,
  input,
}: {
  label: string;
  input: InputHTMLAttributes<HTMLInputElement>;
}) {
  return (
    <div className={styles.inputGroup}>
      <label htmlFor={input.id}>{label}</label>
      <input {...input} />
    </div>
  );
}

export default Input;
