begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
comment|/**  * Exception thrown in case of last file operation failed.  */
end_comment

begin_class
DECL|class|GenericFileOperationFailedException
specifier|public
class|class
name|GenericFileOperationFailedException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|64176625836814418L
decl_stmt|;
DECL|field|code
specifier|private
specifier|final
name|int
name|code
decl_stmt|;
DECL|field|reason
specifier|private
specifier|final
name|String
name|reason
decl_stmt|;
DECL|method|GenericFileOperationFailedException (String message)
specifier|public
name|GenericFileOperationFailedException
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
name|this
operator|.
name|code
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|reason
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|GenericFileOperationFailedException (String message, Throwable cause)
specifier|public
name|GenericFileOperationFailedException
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
name|this
operator|.
name|code
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|reason
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|GenericFileOperationFailedException (int code, String reason)
specifier|public
name|GenericFileOperationFailedException
parameter_list|(
name|int
name|code
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|super
argument_list|(
literal|"File operation failed"
operator|+
operator|(
name|reason
operator|!=
literal|null
condition|?
literal|": "
operator|+
name|reason
else|:
literal|""
operator|)
operator|+
literal|". Code: "
operator|+
name|code
argument_list|)
expr_stmt|;
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
DECL|method|GenericFileOperationFailedException (int code, String reason, Throwable cause)
specifier|public
name|GenericFileOperationFailedException
parameter_list|(
name|int
name|code
parameter_list|,
name|String
name|reason
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"File operation failed"
operator|+
operator|(
name|reason
operator|!=
literal|null
condition|?
literal|": "
operator|+
name|reason
else|:
literal|""
operator|)
operator|+
literal|". Code: "
operator|+
name|code
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
DECL|method|GenericFileOperationFailedException (int code, String reason, String message)
specifier|public
name|GenericFileOperationFailedException
parameter_list|(
name|int
name|code
parameter_list|,
name|String
name|reason
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|code
argument_list|,
name|reason
operator|+
literal|" "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|GenericFileOperationFailedException (int code, String reason, String message, Throwable cause)
specifier|public
name|GenericFileOperationFailedException
parameter_list|(
name|int
name|code
parameter_list|,
name|String
name|reason
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
name|code
argument_list|,
name|reason
operator|+
literal|" "
operator|+
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return the file failure code (if any)      */
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
comment|/**      * Return the file failure reason (if any)      */
DECL|method|getReason ()
specifier|public
name|String
name|getReason
parameter_list|()
block|{
return|return
name|reason
return|;
block|}
block|}
end_class

end_unit

