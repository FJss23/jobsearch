import { Tag as TagType } from "../../types/Tag";
import Tag from "./Tag";
import styles from "./TagList.module.css";

function TagList({ tags }: { tags: TagType[] }) {
  return (
    <>
      <ul className={styles.tags}>
        {tags.map((tag) => (
          <li key={tag.id}>
            <Tag tag={tag} />
          </li>
        ))}
      </ul>
    </>
  );
}

export default TagList;
