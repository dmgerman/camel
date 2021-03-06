begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|decorators
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
name|Endpoint
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
name|Exchange
import|;
end_import

begin_class
DECL|class|TimerSegmentDecorator
specifier|public
class|class
name|TimerSegmentDecorator
extends|extends
name|AbstractSegmentDecorator
block|{
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|"timer"
return|;
block|}
annotation|@
name|Override
DECL|method|getOperationName (Exchange exchange, Endpoint endpoint)
specifier|public
name|String
name|getOperationName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|Object
name|name
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TIMER_NAME
argument_list|)
decl_stmt|;
return|return
name|name
operator|instanceof
name|String
condition|?
operator|(
name|String
operator|)
name|name
else|:
name|super
operator|.
name|getOperationName
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
block|}
end_class

end_unit

