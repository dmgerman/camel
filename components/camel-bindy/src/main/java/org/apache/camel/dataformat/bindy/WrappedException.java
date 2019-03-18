begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
package|;
end_package

begin_comment
comment|/**  * A {@link RuntimeException} which wraps a checked exception. This is necessary when dealing with streams,  * since the API does not allow catching or propagating a checked exception.  */
end_comment

begin_class
DECL|class|WrappedException
specifier|public
class|class
name|WrappedException
extends|extends
name|RuntimeException
block|{
DECL|field|exception
specifier|private
specifier|final
name|Exception
name|exception
decl_stmt|;
comment|/**      * Mandatory constructor.      * @param wrappedException the checked exception being passed in      */
DECL|method|WrappedException (Exception wrappedException)
specifier|public
name|WrappedException
parameter_list|(
name|Exception
name|wrappedException
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|wrappedException
expr_stmt|;
block|}
DECL|method|getWrappedException ()
specifier|public
name|Exception
name|getWrappedException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
block|}
end_class

end_unit

