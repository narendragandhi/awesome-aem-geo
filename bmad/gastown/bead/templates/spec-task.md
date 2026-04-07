---
id: ${workflow_id}-spec-${sequence}
workflow_id: ${workflow_id}
type: specification
agent: docs
status: pending
priority: ${priority}
created: ${timestamp}
updated: ${timestamp}
depends_on: []
blocks: [${impl_issue_id}, ${test_issue_id}]
---

# ${component_name} Specification

## Overview

**Component/Feature**: ${component_name}
**Type**: ${component_type}
**Purpose**: ${one_sentence_purpose}

## Context

${detailed_context}

### Business Requirements

1. ${requirement_1}
2. ${requirement_2}
3. ${requirement_3}

### Technical Constraints

- AEM Version: ${aem_version}
- Dependencies: ${dependencies}
- Compatible with: ${compatibility}

## Functional Specification

### Core Features

| Feature | Description | Priority |
|---------|-------------|----------|
| ${feature_1} | ${description} | ${priority} |
| ${feature_2} | ${description} | ${priority} |

### User Interactions

${user_interactions}

### Data Model

```java
public interface ${ComponentName}Model {
    // Getters for all exported properties
}
```

### Edge Cases

1. ${edge_case_1}
2. ${edge_case_2}

## Non-Functional Requirements

### Performance

- ${performance_requirement_1}
- ${performance_requirement_2}

### Security

- ${security_requirement}

### Accessibility

- ${accessibility_requirement}

## Acceptance Criteria

- [ ] ${acceptance_criteria_1}
- [ ] ${acceptance_criteria_2}
- [ ] ${acceptance_criteria_3}
- [ ] Code compiles without errors
- [ ] All unit tests pass
- [ ] Follows AEM coding standards

## Technical Design

### File Structure

| File Type | Path |
|-----------|------|
| Sling Model | `core/src/main/java/.../models/${ComponentName}Model.java` |
| HTL Template | `ui.apps/.../components/${component_type}/${component_name}/${component_name}.html` |
| Spec Test | `core/src/test/java/.../${ComponentName}SpecTest.java` |
| Unit Test | `core/src/test/java/.../${ComponentName}ModelTest.java` |

### Dependencies

```xml
<dependency>
    <groupId>com.adobe.aem</groupId>
    <artifactId>aem-sdk-api</artifactId>
</dependency>
```

## Progress Log

### ${timestamp}
Specification created by Mayor during ${workflow_name} workflow.

## Notes

<!-- Additional notes for implementation team -->
