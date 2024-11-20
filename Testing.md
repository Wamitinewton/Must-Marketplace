
# Android Testing Guide

## Overview

This file contains the guide to the testing approach followed for the MUST-MARKET Android
application.

## Test Distribution

- **Unit Tests**: 70% (Primary focus)
- **Integration Tests**: 20%
- **UI Tests**: 10%

## When to Write Tests

### Unit Tests Required For:

- ViewModels
- Use Cases (Domain layer)
- Utility Classes (String manipulation, math operations, etc.)
- Repositories
- Platform-independent components

## Testing Tools

### MockK

We use MockK as our primary mocking framework for Kotlin-based testing.

#### Setup

Add the following dependencies to your module's `build.gradle`:

```gradle
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation("android.arch.core:core-testing:1.1.1")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
```

#### Key MockK Features

1. **Basic Mocking**

```kotlin
// Create a mock
private val repository = mockk<UserRepository>()

// Relaxed mock (returns default values)
private val logger = mockk<Logger>(relaxed = true)
```

2. **Behavior Stubbing**

```kotlin
// For regular functions
every { repository.getUser(any()) } returns user

// For suspend functions
coEvery { repository.fetchUserData(any()) } returns userData
```

3. **Verification**

```kotlin
// Verify exact number of calls
verify(exactly = 1) { repository.getUser(userId) }

// Verify minimum calls
verify(atLeast = 1) { repository.saveUser(any()) }

// Verify call order
verifyOrder {
    repository.getUser(userId)
    repository.saveUser(user)
}
```

4. **Argument Capture**

```kotlin
val slot = slot<User>()
every { repository.saveUser(capture(slot)) } returns Unit

// Later verify the captured value
assertEquals(expectedUser, slot.captured)
```

## Best Practices

### 1. Test Structure

Follow the AAA (Arrange-Act-Assert) pattern:

```kotlin
@Test
fun `test user creation`() {
    // Arrange
    every { repository.createUser(any()) } returns User()

    // Act
    val result = viewModel.createUser(userData)

    // Assert
    verify { repository.createUser(userData) }
    assertEquals(expectedUser, result)
}
```

### 2. Naming Conventions

Use descriptive test names that indicate:

- The scenario being tested
- The expected behavior
- The conditions (if relevant)

Example:

```kotlin
@Test
fun `should return error when network is unavailable`() {
}
```

### 3. Test Independence

- Each test should be independent and self-contained
- Avoid test interdependencies
- Clean up after tests using `@After` annotations

### 4. Mock Behavior Guidelines

- Mock external dependencies
- Don't mock data structures or value objects
- Use `relaxed = true` sparingly and only when necessary

## Testing UI Components

### Espresso

For UI testing, refer to our separate UI testing guide. Key points:

- Test user interactions
- Verify view states
- Check navigation flows

## Coverage Goals

- Minimum unit test coverage: 80%
- Critical path coverage: 100%
- Business logic coverage: 90%

To check coverage:

1. Right-click on test class
2. Select "Run with Coverage"

## Common Pitfalls to Avoid

1. Over-mocking
2. Testing implementation details
3. Brittle tests
4. Ignored tests without documentation
5. Complex test setup

## Resources

- [Official Android Testing Documentation](https://developer.android.com/training/testing)
- [MockK Documentation](https://mockk.io/)
- [Espresso Testing](https://developer.android.com/training/testing/espresso)

## Contributing

When adding new features:

1. Write tests before or alongside implementation
2. Maintain test coverage goals
3. Follow existing patterns and conventions
4. Update this guide if new patterns emerge
