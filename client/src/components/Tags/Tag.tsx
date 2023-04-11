import { Link } from "react-router-dom";
import { Tag as TagType } from "../../types/Tag";
import styles from "./Tag.module.css";

function Tag({ tag }: { tag: TagType }) {
  return (
    <Link to={"#"}>
      <span className={styles.tag}>{tag.name}</span>
    </Link>
  );
}

export default Tag;
