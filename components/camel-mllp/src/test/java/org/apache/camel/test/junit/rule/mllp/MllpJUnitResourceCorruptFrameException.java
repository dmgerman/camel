begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.junit.rule.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit
operator|.
name|rule
operator|.
name|mllp
package|;
end_package

begin_comment
comment|/**  * Thrown when one of the MLLP JUnit Rules encounters a corrupt MLLP Frame  */
end_comment

begin_class
DECL|class|MllpJUnitResourceCorruptFrameException
specifier|public
class|class
name|MllpJUnitResourceCorruptFrameException
extends|extends
name|MllpJUnitResourceException
block|{
DECL|method|MllpJUnitResourceCorruptFrameException (String message)
specifier|public
name|MllpJUnitResourceCorruptFrameException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpJUnitResourceCorruptFrameException (String message, Throwable cause)
specifier|public
name|MllpJUnitResourceCorruptFrameException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpJUnitResourceCorruptFrameException (Throwable cause)
specifier|public
name|MllpJUnitResourceCorruptFrameException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpJUnitResourceCorruptFrameException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
specifier|public
name|MllpJUnitResourceCorruptFrameException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|,
name|boolean
name|enableSuppression
parameter_list|,
name|boolean
name|writableStackTrace
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|,
name|enableSuppression
argument_list|,
name|writableStackTrace
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

