begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.command
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|command
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|ITestConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|JUnitCore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|notification
operator|.
name|Failure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|notification
operator|.
name|RunListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|BeanDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|ClassPathScanningCandidateComponentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|type
operator|.
name|filter
operator|.
name|RegexPatternTypeFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * A command that executes all unit tests contained in the module.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"unittest"
argument_list|)
DECL|class|UnitTestCommand
specifier|public
class|class
name|UnitTestCommand
extends|extends
name|AbstractTestCommand
implements|implements
name|Command
block|{
DECL|field|logger
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Override
DECL|method|executeTest (final ITestConfig config, String component)
specifier|public
name|UnitTestResult
name|executeTest
parameter_list|(
specifier|final
name|ITestConfig
name|config
parameter_list|,
name|String
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Spring-Boot test configuration {}"
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|config
operator|.
name|getUnitTestInclusionPattern
argument_list|()
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Scaning the classpath for test classes"
argument_list|)
expr_stmt|;
name|ClassPathScanningCandidateComponentProvider
name|scanner
init|=
operator|new
name|ClassPathScanningCandidateComponentProvider
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|scanner
operator|.
name|addIncludeFilter
argument_list|(
operator|new
name|RegexPatternTypeFilter
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|BeanDefinition
argument_list|>
name|defs
init|=
name|scanner
operator|.
name|findCandidateComponents
argument_list|(
name|config
operator|.
name|getUnitTestBasePackage
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|testClasses
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|BeanDefinition
name|bd
range|:
name|defs
control|)
block|{
name|testClasses
operator|.
name|add
argument_list|(
name|bd
operator|.
name|getBeanClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getUnitTestExclusionPattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Pattern
name|exclusionPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|config
operator|.
name|getUnitTestExclusionPattern
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|testClasses
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|cn
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|exclusionPattern
operator|.
name|matcher
argument_list|(
name|cn
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Excluding Test Class: {}"
argument_list|,
name|cn
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|cn
range|:
name|testClasses
control|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|Class
operator|.
name|forName
argument_list|(
name|cn
argument_list|)
decl_stmt|;
if|if
condition|(
name|isAdmissible
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Found admissible test class: {}"
argument_list|,
name|cn
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Test class {} has thrown an exception during initialization"
argument_list|,
name|cn
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Exception for test cass "
operator|+
name|cn
operator|+
literal|" is:"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"Run JUnit tests on {} test classes"
argument_list|,
name|classes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JUnitCore
name|runner
init|=
operator|new
name|JUnitCore
argument_list|()
decl_stmt|;
name|runner
operator|.
name|addListener
argument_list|(
operator|new
name|RunListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|testStarted
parameter_list|(
name|Description
name|description
parameter_list|)
throws|throws
name|Exception
block|{
name|disableJmx
argument_list|(
name|config
operator|.
name|getJmxDisabledNames
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Result
name|result
init|=
name|runner
operator|.
name|run
argument_list|(
name|classes
operator|.
name|toArray
argument_list|(
operator|new
name|Class
index|[]
block|{}
argument_list|)
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|config
operator|.
name|getModuleName
argument_list|()
operator|+
literal|" unit tests. "
operator|+
literal|"Success: "
operator|+
name|result
operator|.
name|wasSuccessful
argument_list|()
operator|+
literal|" - Test Run: "
operator|+
name|result
operator|.
name|getRunCount
argument_list|()
operator|+
literal|" - Failures: "
operator|+
name|result
operator|.
name|getFailureCount
argument_list|()
operator|+
literal|" - Ignored Tests: "
operator|+
name|result
operator|.
name|getIgnoreCount
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Failure
name|f
range|:
name|result
operator|.
name|getFailures
argument_list|()
control|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Failed test description: {}"
argument_list|,
name|f
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|warn
argument_list|(
literal|"Message: {}"
argument_list|,
name|f
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|f
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Exception thrown from test"
argument_list|,
name|f
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|result
operator|.
name|wasSuccessful
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Some unit tests failed ("
operator|+
name|result
operator|.
name|getFailureCount
argument_list|()
operator|+
literal|"/"
operator|+
name|result
operator|.
name|getRunCount
argument_list|()
operator|+
literal|"), check the logs for more details"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|.
name|getRunCount
argument_list|()
operator|==
literal|0
operator|&&
name|config
operator|.
name|getUnitTestsExpectedNumber
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"No tests have been found"
argument_list|)
expr_stmt|;
block|}
name|Integer
name|expectedTests
init|=
name|config
operator|.
name|getUnitTestsExpectedNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedTests
operator|!=
literal|null
operator|&&
name|expectedTests
operator|!=
name|result
operator|.
name|getRunCount
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Wrong number of tests: expected "
operator|+
name|expectedTests
operator|+
literal|" found "
operator|+
name|result
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|UnitTestResult
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|disableJmx (Set<String> disabledJmx)
specifier|private
name|void
name|disableJmx
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|disabledJmx
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Disabling JMX names: {}"
argument_list|,
name|disabledJmx
argument_list|)
expr_stmt|;
for|for
control|(
name|MBeanServer
name|server
range|:
name|getMBeanServers
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|jmxName
range|:
name|disabledJmx
control|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Disabling JMX query {}"
argument_list|,
name|jmxName
argument_list|)
expr_stmt|;
name|ObjectName
name|oName
init|=
operator|new
name|ObjectName
argument_list|(
name|jmxName
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|names
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|server
operator|.
name|queryNames
argument_list|(
name|oName
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|names
control|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Disabled JMX name {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|server
operator|.
name|unregisterMBean
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getMBeanServers ()
specifier|private
name|List
argument_list|<
name|MBeanServer
argument_list|>
name|getMBeanServers
parameter_list|()
block|{
name|List
argument_list|<
name|MBeanServer
argument_list|>
name|servers
init|=
name|MBeanServerFactory
operator|.
name|findMBeanServer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|servers
operator|==
literal|null
condition|)
block|{
name|servers
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
return|return
name|servers
return|;
block|}
DECL|method|isAdmissible (Class<?> testClass)
specifier|private
name|boolean
name|isAdmissible
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
block|{
if|if
condition|(
name|testClass
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"org.apache.camel.itest.springboot"
argument_list|)
condition|)
block|{
comment|// no tests from the integration test suite
return|return
literal|false
return|;
block|}
name|URL
name|location
init|=
name|testClass
operator|.
name|getResource
argument_list|(
literal|"/"
operator|+
name|testClass
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|".class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|location
operator|!=
literal|null
condition|)
block|{
name|int
name|firstLevel
init|=
name|location
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"!/"
argument_list|)
decl_stmt|;
name|int
name|lastLevel
init|=
name|location
operator|.
name|toString
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|"!/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstLevel
operator|>=
literal|0
operator|&&
name|lastLevel
operator|>=
literal|0
operator|&&
name|firstLevel
operator|!=
name|lastLevel
condition|)
block|{
comment|// test class is in a nested jar, skipping
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

