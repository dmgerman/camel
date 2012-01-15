begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MyApplicationException
specifier|public
class|class
name|MyApplicationException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|code
specifier|private
specifier|final
name|int
name|code
decl_stmt|;
DECL|method|MyApplicationException (String message, int code)
specifier|public
name|MyApplicationException
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|code
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
block|}
DECL|method|getCode ()
specifier|public
name|int
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
block|}
end_class

end_unit

