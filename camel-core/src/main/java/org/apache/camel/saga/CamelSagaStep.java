begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.saga
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|saga
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|Expression
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

begin_comment
comment|/**  * Defines the configuration of a saga step.  */
end_comment

begin_class
DECL|class|CamelSagaStep
specifier|public
class|class
name|CamelSagaStep
block|{
DECL|field|compensation
specifier|private
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|compensation
decl_stmt|;
DECL|field|completion
specifier|private
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|completion
decl_stmt|;
DECL|field|options
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Expression
argument_list|>
name|options
decl_stmt|;
DECL|field|timeoutInMilliseconds
specifier|private
name|Optional
argument_list|<
name|Long
argument_list|>
name|timeoutInMilliseconds
decl_stmt|;
DECL|method|CamelSagaStep (Optional<Endpoint> compensation, Optional<Endpoint> completion, Map<String, Expression> options, Optional<Long> timeoutInMilliseconds)
specifier|public
name|CamelSagaStep
parameter_list|(
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|compensation
parameter_list|,
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|completion
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Expression
argument_list|>
name|options
parameter_list|,
name|Optional
argument_list|<
name|Long
argument_list|>
name|timeoutInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|compensation
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|compensation
argument_list|,
literal|"compensation"
argument_list|)
expr_stmt|;
name|this
operator|.
name|completion
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|completion
argument_list|,
literal|"completionCallbacks"
argument_list|)
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|options
argument_list|,
literal|"options"
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeoutInMilliseconds
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|timeoutInMilliseconds
argument_list|,
literal|"timeoutInMilliseconds"
argument_list|)
expr_stmt|;
block|}
DECL|method|getCompensation ()
specifier|public
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|getCompensation
parameter_list|()
block|{
return|return
name|compensation
return|;
block|}
DECL|method|getCompletion ()
specifier|public
name|Optional
argument_list|<
name|Endpoint
argument_list|>
name|getCompletion
parameter_list|()
block|{
return|return
name|completion
return|;
block|}
DECL|method|getOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Expression
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
DECL|method|getTimeoutInMilliseconds ()
specifier|public
name|Optional
argument_list|<
name|Long
argument_list|>
name|getTimeoutInMilliseconds
parameter_list|()
block|{
return|return
name|timeoutInMilliseconds
return|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
operator|!
name|compensation
operator|.
name|isPresent
argument_list|()
operator|&&
operator|!
name|completion
operator|.
name|isPresent
argument_list|()
operator|&&
name|options
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|timeoutInMilliseconds
operator|.
name|isPresent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

