begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|xml
package|;
end_package

begin_class
DECL|class|IllegalActionException
specifier|public
class|class
name|IllegalActionException
extends|extends
name|IllegalArgumentException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2166507687211986107L
decl_stmt|;
DECL|field|actionName
specifier|private
specifier|final
name|String
name|actionName
decl_stmt|;
DECL|field|previousAction
specifier|private
specifier|final
name|String
name|previousAction
decl_stmt|;
DECL|method|IllegalActionException (String actionName, String previousAction)
specifier|public
name|IllegalActionException
parameter_list|(
name|String
name|actionName
parameter_list|,
name|String
name|previousAction
parameter_list|)
block|{
name|super
argument_list|(
literal|"Illegal route."
argument_list|)
expr_stmt|;
name|this
operator|.
name|actionName
operator|=
name|actionName
expr_stmt|;
name|this
operator|.
name|previousAction
operator|=
name|previousAction
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
name|String
name|errorContext
init|=
name|previousAction
operator|==
literal|null
condition|?
literal|"as the starting action."
else|:
literal|"after action '"
operator|+
name|previousAction
operator|+
literal|"'."
decl_stmt|;
return|return
name|super
operator|.
name|getMessage
argument_list|()
operator|+
literal|"The action '"
operator|+
name|actionName
operator|+
literal|"' cannot be used "
operator|+
name|errorContext
return|;
block|}
block|}
end_class

end_unit

