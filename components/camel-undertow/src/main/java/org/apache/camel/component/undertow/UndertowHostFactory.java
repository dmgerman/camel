begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_comment
comment|/**  * A factory to create an undertow host  */
end_comment

begin_interface
DECL|interface|UndertowHostFactory
specifier|public
interface|interface
name|UndertowHostFactory
block|{
comment|/**      * Create or get an undertow host      */
DECL|method|createUndertowHost ()
name|UndertowHost
name|createUndertowHost
parameter_list|()
function_decl|;
DECL|class|Locator
specifier|public
class|class
name|Locator
block|{
DECL|field|globalFactory
specifier|private
specifier|static
name|UndertowHostFactory
name|globalFactory
decl_stmt|;
DECL|method|getUndertowHostFactory ()
specifier|public
specifier|static
name|UndertowHostFactory
name|getUndertowHostFactory
parameter_list|()
block|{
return|return
name|globalFactory
return|;
block|}
DECL|method|setUndertowHostFactory (UndertowHostFactory factory)
specifier|public
specifier|static
name|void
name|setUndertowHostFactory
parameter_list|(
name|UndertowHostFactory
name|factory
parameter_list|)
block|{
name|globalFactory
operator|=
name|factory
expr_stmt|;
block|}
block|}
block|}
end_interface

end_unit

