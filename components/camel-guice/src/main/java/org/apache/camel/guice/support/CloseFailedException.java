begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|support
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|internal
operator|.
name|Errors
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|spi
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Indicates that an attempt to close an injector or scope failed closing one or  * more bindings.  *   */
end_comment

begin_class
DECL|class|CloseFailedException
specifier|public
class|class
name|CloseFailedException
extends|extends
name|IOException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4794716198859801415L
decl_stmt|;
DECL|field|messages
specifier|private
specifier|final
name|List
argument_list|<
name|Message
argument_list|>
name|messages
decl_stmt|;
DECL|method|CloseFailedException (List<Message> messages)
specifier|public
name|CloseFailedException
parameter_list|(
name|List
argument_list|<
name|Message
argument_list|>
name|messages
parameter_list|)
block|{
name|super
argument_list|(
name|Errors
operator|.
name|format
argument_list|(
literal|"Close errors"
argument_list|,
name|messages
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|messages
operator|=
name|messages
expr_stmt|;
block|}
DECL|method|getMessages ()
specifier|public
name|List
argument_list|<
name|Message
argument_list|>
name|getMessages
parameter_list|()
block|{
return|return
name|messages
return|;
block|}
block|}
end_class

end_unit

