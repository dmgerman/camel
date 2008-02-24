begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|IOHelper
specifier|public
class|class
name|IOHelper
block|{
comment|/**      * A factory method which creates an {@link IOException} from the given      * exception and message      */
DECL|method|createIOException (Throwable cause)
specifier|public
specifier|static
name|IOException
name|createIOException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
return|return
name|createIOException
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
argument_list|)
return|;
block|}
comment|/**      * A factory method which creates an {@link IOException} from the given      * exception and message      */
DECL|method|createIOException (String message, Throwable cause)
specifier|public
specifier|static
name|IOException
name|createIOException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|IOException
name|answer
init|=
operator|new
name|IOException
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|answer
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

