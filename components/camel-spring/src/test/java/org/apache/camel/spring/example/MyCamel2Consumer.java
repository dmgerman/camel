begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|example
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
name|Consume
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
name|ProducerTemplate
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
name|util
operator|.
name|ObjectHelper
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

begin_class
DECL|class|MyCamel2Consumer
specifier|public
class|class
name|MyCamel2Consumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MyCamel2Consumer
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|,
name|context
operator|=
literal|"camel-2"
argument_list|)
DECL|field|destination
specifier|private
name|ProducerTemplate
name|destination
decl_stmt|;
annotation|@
name|Consume
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|,
name|context
operator|=
literal|"camel-2"
argument_list|)
DECL|method|doSomething (String body)
specifier|public
name|void
name|doSomething
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|destination
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Received body: "
operator|+
name|body
argument_list|)
expr_stmt|;
name|destination
operator|.
name|sendBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|getDestination ()
specifier|public
name|ProducerTemplate
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
DECL|method|setDestination (ProducerTemplate destination)
specifier|public
name|void
name|setDestination
parameter_list|(
name|ProducerTemplate
name|destination
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
block|}
end_class

end_unit

