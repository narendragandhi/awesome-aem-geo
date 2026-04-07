---
id: ${workflow_id}-test-${sequence}
workflow_id: ${workflow_id}
type: testing
agent: tester
status: pending
priority: ${priority}
created: ${timestamp}
updated: ${timestamp}
depends_on: [${spec_issue_id}, ${impl_issue_id}]
blocks: [${review_issue_id}]
---

# Test ${component_name} Component

## Context

Write tests for ${component_name} AEM component as specified in SPEC-${spec_issue_id}.

**Implementation Reference**: bmad/gastown/bead/.issues/coder/${impl_issue_id}.md

## Test Requirements

### Unit Tests

- [ ] Test all model getters
- [ ] Test null/edge case handling
- [ ] Test JSON export format
- [ ] Test i18n support

### Integration Tests (if applicable)

- [ ] Test HTL rendering
- [ ] Test dialog configuration

### Test Coverage Target

- Minimum 80% code coverage

## Technical Details

### Test Class Location

| Test Type | Path |
|-----------|------|
| Spec Test | `core/src/test/java/com/awesomeaem/geo/${ComponentName}SpecTest.java` |
| Unit Test | `core/src/test/java/com/awesomeaem/geo/${ComponentName}ModelTest.java` |

### Test Framework

- JUnit 5
- Mockito
- AEM Mocks (wcm.io)

## Acceptance Criteria

- [ ] All tests pass
- [ ] 80%+ code coverage
- [ ] Tests follow TDD approach
- [ ] Tests are maintainable

## Progress Log

### ${timestamp}
Issue created by Mayor during ${workflow_name} workflow.

## Handoff Notes

<!-- Document any issues or observations from implementation -->

## Test Results

<!-- Updated as tests are written -->

## Related Issues

- Specification: #${spec_issue_id}
- Implementation: #${impl_issue_id}
- Review: #${review_issue_id}
