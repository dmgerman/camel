begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Various tests for the uri builder  */
end_comment

begin_class
DECL|class|JMXUriBuilderTest
specifier|public
class|class
name|JMXUriBuilderTest
block|{
annotation|@
name|Test
DECL|method|defaultsToPlatform ()
specifier|public
name|void
name|defaultsToPlatform
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:platform"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|remote ()
specifier|public
name|void
name|remote
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withServerName ()
specifier|public
name|void
name|withServerName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withServerName
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|format ()
specifier|public
name|void
name|format
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:platform?format=raw"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withFormat
argument_list|(
literal|"raw"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|credentials ()
specifier|public
name|void
name|credentials
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:platform?user=me&password=pass"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withUser
argument_list|(
literal|"me"
argument_list|)
operator|.
name|withPassword
argument_list|(
literal|"pass"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|objectName ()
specifier|public
name|void
name|objectName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:platform?objectDomain=myDomain&objectName=oname"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withObjectDomain
argument_list|(
literal|"myDomain"
argument_list|)
operator|.
name|withObjectName
argument_list|(
literal|"oname"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|notificationFilter ()
specifier|public
name|void
name|notificationFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:platform?notificationFilter=#foo"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withNotificationFilter
argument_list|(
literal|"#foo"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|handback ()
specifier|public
name|void
name|handback
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:platform?handback=#hb"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withHandback
argument_list|(
literal|"#hb"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|objectProperties ()
specifier|public
name|void
name|objectProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"one"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"two"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jmx:platform?key.one=1&key.two=2"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withObjectProperties
argument_list|(
name|map
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withObjectPropertiesReference ()
specifier|public
name|void
name|withObjectPropertiesReference
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:platform?objectProperties=#op"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withObjectPropertiesReference
argument_list|(
literal|"#op"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|withObjectPropertiesReferenceSansHashmark ()
specifier|public
name|void
name|withObjectPropertiesReferenceSansHashmark
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"jmx:platform?objectProperties=#op"
argument_list|,
operator|new
name|JMXUriBuilder
argument_list|()
operator|.
name|withObjectPropertiesReference
argument_list|(
literal|"op"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

