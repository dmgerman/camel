begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
package|;
end_package

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|Address
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
name|Converter
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|AtomixTypeConverter
specifier|public
specifier|final
class|class
name|AtomixTypeConverter
block|{
DECL|method|AtomixTypeConverter ()
specifier|private
name|AtomixTypeConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toAddress (String address)
specifier|public
specifier|static
name|Address
name|toAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
operator|new
name|Address
argument_list|(
name|address
argument_list|)
return|;
block|}
block|}
end_class

end_unit

