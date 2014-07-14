begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.patterns
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|patterns
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MyProduceBean
specifier|public
class|class
name|MyProduceBean
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|sender
name|MySender
name|sender
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
name|ProducerTemplate
name|template
decl_stmt|;
DECL|method|doSomething (String body)
specifier|public
name|void
name|doSomething
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|sender
operator|.
name|send
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|getProducerTemplate ()
specifier|public
name|ProducerTemplate
name|getProducerTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
block|}
end_class

end_unit

