begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.exec
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|exec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * Exception thrown when there is an execution failure.  */
end_comment

begin_class
DECL|class|ExecException
specifier|public
class|class
name|ExecException
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
literal|7808703605527644487L
decl_stmt|;
DECL|field|exitValue
specifier|private
specifier|final
name|int
name|exitValue
decl_stmt|;
DECL|field|stdout
specifier|private
specifier|final
name|InputStream
name|stdout
decl_stmt|;
DECL|field|stderr
specifier|private
specifier|final
name|InputStream
name|stderr
decl_stmt|;
DECL|method|ExecException (String message, final InputStream stdout, final InputStream stderr, final int exitValue)
specifier|public
name|ExecException
parameter_list|(
name|String
name|message
parameter_list|,
specifier|final
name|InputStream
name|stdout
parameter_list|,
specifier|final
name|InputStream
name|stderr
parameter_list|,
specifier|final
name|int
name|exitValue
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|exitValue
operator|=
name|exitValue
expr_stmt|;
name|this
operator|.
name|stderr
operator|=
name|stderr
expr_stmt|;
name|this
operator|.
name|stdout
operator|=
name|stdout
expr_stmt|;
block|}
DECL|method|ExecException (String message, final InputStream stdout, final InputStream stderr, final int exitValue, Throwable cause)
specifier|public
name|ExecException
parameter_list|(
name|String
name|message
parameter_list|,
specifier|final
name|InputStream
name|stdout
parameter_list|,
specifier|final
name|InputStream
name|stderr
parameter_list|,
specifier|final
name|int
name|exitValue
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
name|exitValue
operator|=
name|exitValue
expr_stmt|;
name|this
operator|.
name|stderr
operator|=
name|stderr
expr_stmt|;
name|this
operator|.
name|stdout
operator|=
name|stdout
expr_stmt|;
block|}
DECL|method|getExitValue ()
specifier|public
name|int
name|getExitValue
parameter_list|()
block|{
return|return
name|exitValue
return|;
block|}
DECL|method|getStdout ()
specifier|public
name|InputStream
name|getStdout
parameter_list|()
block|{
return|return
name|stdout
return|;
block|}
DECL|method|getStderr ()
specifier|public
name|InputStream
name|getStderr
parameter_list|()
block|{
return|return
name|stderr
return|;
block|}
block|}
end_class

end_unit

