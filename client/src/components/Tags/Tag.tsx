import { Tag as TagType } from "../../types/Tag";
import styles from "./Tag.module.css";

function Tag({ tag }: { tag: TagType }) {
  return (
    <span className={styles.tag}>
      <mark>{tag.name}</mark>
    </span>
  );
}

export default Tag;
