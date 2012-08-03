begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
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
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * TODO Add Class documentation for IllegalHeaderException  *  */
end_comment

begin_class
DECL|class|IllegalHeaderException
specifier|public
class|class
name|IllegalHeaderException
extends|extends
name|RuntimeCamelException
block|{
comment|/**      *       */
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1084936154059689378L
decl_stmt|;
comment|/**      * TODO Add Constructor Javadoc      *      */
DECL|method|IllegalHeaderException ()
specifier|public
name|IllegalHeaderException
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
comment|// TODO Auto-generated constructor stub
block|}
comment|/**      * TODO Add Constructor Javadoc      *      * @param message      * @param cause      */
DECL|method|IllegalHeaderException (String message, Throwable cause)
specifier|public
name|IllegalHeaderException
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
comment|// TODO Auto-generated constructor stub
block|}
comment|/**      * TODO Add Constructor Javadoc      *      * @param message      */
DECL|method|IllegalHeaderException (String message)
specifier|public
name|IllegalHeaderException
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
comment|// TODO Auto-generated constructor stub
block|}
comment|/**      * TODO Add Constructor Javadoc      *      * @param cause      */
DECL|method|IllegalHeaderException (Throwable cause)
specifier|public
name|IllegalHeaderException
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
comment|// TODO Auto-generated constructor stub
block|}
block|}
end_class

end_unit

