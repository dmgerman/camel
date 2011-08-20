begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|Body
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
name|Handler
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
name|builder
operator|.
name|RouteBuilder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JaxbErrorLogTest
specifier|public
class|class
name|JaxbErrorLogTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testErrorHandling ()
specifier|public
name|void
name|testErrorHandling
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the 2nd message is set to fail, but the 4 others should be routed
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
comment|// FailingBean will cause message at index 2 to throw exception
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|sendBody
argument_list|(
literal|"seda:test"
argument_list|,
operator|new
name|CannotMarshal
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:test"
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|FailingBean
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:end"
argument_list|,
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|FailingBean
specifier|public
specifier|static
specifier|final
class|class
name|FailingBean
block|{
annotation|@
name|Handler
DECL|method|handle (@ody CannotMarshal body)
specifier|public
name|void
name|handle
parameter_list|(
annotation|@
name|Body
name|CannotMarshal
name|body
parameter_list|)
block|{
if|if
condition|(
name|body
operator|.
name|getMessageNo
argument_list|()
operator|==
literal|2
condition|)
block|{
comment|// fail on second message
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Kaboom"
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * This class will throw RuntimeException on JAXB marshal      */
annotation|@
name|XmlRootElement
DECL|class|CannotMarshal
specifier|public
specifier|static
specifier|final
class|class
name|CannotMarshal
block|{
DECL|field|messageNo
specifier|private
name|int
name|messageNo
decl_stmt|;
DECL|method|CannotMarshal ()
specifier|public
name|CannotMarshal
parameter_list|()
block|{         }
DECL|method|CannotMarshal (int messageNo)
specifier|public
name|CannotMarshal
parameter_list|(
name|int
name|messageNo
parameter_list|)
block|{
name|this
operator|.
name|messageNo
operator|=
name|messageNo
expr_stmt|;
block|}
DECL|method|getMessageNo ()
specifier|public
name|int
name|getMessageNo
parameter_list|()
block|{
return|return
name|messageNo
return|;
block|}
DECL|method|setMessageNo (int messageNo)
specifier|public
name|void
name|setMessageNo
parameter_list|(
name|int
name|messageNo
parameter_list|)
block|{
name|this
operator|.
name|messageNo
operator|=
name|messageNo
expr_stmt|;
block|}
DECL|method|setUhoh (String name)
specifier|public
name|void
name|setUhoh
parameter_list|(
name|String
name|name
parameter_list|)
block|{         }
DECL|method|getUhoh ()
specifier|public
name|String
name|getUhoh
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can't marshal this"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MessageNo. "
operator|+
name|messageNo
return|;
block|}
block|}
block|}
end_class

end_unit

