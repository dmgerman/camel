begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.soap.name
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|soap
operator|.
name|name
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * Value object to hold type information about parameters and return type of a  * method  */
end_comment

begin_class
DECL|class|TypeInfo
specifier|final
class|class
name|TypeInfo
block|{
DECL|field|typeName
specifier|private
specifier|final
name|String
name|typeName
decl_stmt|;
DECL|field|elName
specifier|private
specifier|final
name|QName
name|elName
decl_stmt|;
comment|/**      * Initialize TypeInfo with given name and resolved element name for a type      *       * @param typeName      * @param elName      */
DECL|method|TypeInfo (final String typeName, final QName elName)
specifier|public
name|TypeInfo
parameter_list|(
specifier|final
name|String
name|typeName
parameter_list|,
specifier|final
name|QName
name|elName
parameter_list|)
block|{
name|this
operator|.
name|typeName
operator|=
name|typeName
expr_stmt|;
name|this
operator|.
name|elName
operator|=
name|elName
expr_stmt|;
block|}
DECL|method|getTypeName ()
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
name|typeName
return|;
block|}
DECL|method|getElName ()
specifier|public
name|QName
name|getElName
parameter_list|()
block|{
return|return
name|elName
return|;
block|}
block|}
end_class

end_unit

