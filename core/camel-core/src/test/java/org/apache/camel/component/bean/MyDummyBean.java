begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|Reader
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
name|Handler
import|;
end_import

begin_class
DECL|class|MyDummyBean
specifier|public
class|class
name|MyDummyBean
block|{
DECL|method|hello (String s)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|s
return|;
block|}
DECL|method|hello (String s, String t)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|t
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|s
operator|+
literal|" and "
operator|+
name|t
return|;
block|}
annotation|@
name|Handler
DECL|method|bye (String s)
specifier|public
name|String
name|bye
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"Bye "
operator|+
name|s
return|;
block|}
DECL|method|bar (String s)
specifier|public
name|String
name|bar
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"String"
return|;
block|}
DECL|method|bar (Reader s)
specifier|public
name|String
name|bar
parameter_list|(
name|Reader
name|s
parameter_list|)
block|{
return|return
literal|"Reader"
return|;
block|}
DECL|method|bar (InputStream s)
specifier|public
name|String
name|bar
parameter_list|(
name|InputStream
name|s
parameter_list|)
block|{
return|return
literal|"InputStream"
return|;
block|}
block|}
end_class

end_unit

