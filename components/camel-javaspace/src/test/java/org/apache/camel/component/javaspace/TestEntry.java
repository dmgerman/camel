begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.javaspace
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|javaspace
package|;
end_package

begin_import
import|import
name|net
operator|.
name|jini
operator|.
name|core
operator|.
name|entry
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * Title: Plain JavaSpaces Example - TestEntry entry class.  *<p>  * Description: Simple entry implementation to be used in write, take and read  * space operations.  */
end_comment

begin_class
DECL|class|TestEntry
specifier|public
class|class
name|TestEntry
implements|implements
name|Entry
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
DECL|field|content
specifier|public
name|String
name|content
decl_stmt|;
DECL|field|id
specifier|public
name|Integer
name|id
decl_stmt|;
comment|/**      * No arguments constructor (mandatory in an entry class).      */
DECL|method|TestEntry ()
specifier|public
name|TestEntry
parameter_list|()
block|{     }
comment|/**      * @return a string containing the message attributes for display.      */
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"TestEntry ID:"
operator|+
name|id
operator|+
literal|" ,TestEntry content: "
operator|+
name|content
return|;
block|}
DECL|method|getId ()
specifier|public
name|Integer
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Integer id)
specifier|public
name|void
name|setId
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit

