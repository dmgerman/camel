begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.entity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
package|;
end_package

begin_enum
DECL|enum|AS2DispositionType
specifier|public
enum|enum
name|AS2DispositionType
block|{
DECL|enumConstant|PROCESSED
name|PROCESSED
argument_list|(
literal|"processed"
argument_list|)
block|,
DECL|enumConstant|FAILED
name|FAILED
argument_list|(
literal|"failed"
argument_list|)
block|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|method|AS2DispositionType (String type)
specifier|private
name|AS2DispositionType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|parseDispositionType (String dispositionTypeString)
specifier|public
specifier|static
name|AS2DispositionType
name|parseDispositionType
parameter_list|(
name|String
name|dispositionTypeString
parameter_list|)
block|{
switch|switch
condition|(
name|dispositionTypeString
condition|)
block|{
case|case
literal|"processed"
case|:
return|return
name|PROCESSED
return|;
case|case
literal|"failed"
case|:
return|return
name|FAILED
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
block|}
end_enum

end_unit

