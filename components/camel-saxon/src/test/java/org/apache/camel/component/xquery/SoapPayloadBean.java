begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|TypeConverter
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|SoapPayloadBean
specifier|public
class|class
name|SoapPayloadBean
block|{
DECL|method|doSomething (@QueryR) Document payload, TypeConverter converter)
specifier|public
name|String
name|doSomething
parameter_list|(
annotation|@
name|XQuery
argument_list|(
literal|"//payload"
argument_list|)
name|Document
name|payload
parameter_list|,
name|TypeConverter
name|converter
parameter_list|)
block|{
comment|// grab the DOM payload
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|payload
argument_list|,
literal|"@XQuery payload"
argument_list|)
expr_stmt|;
comment|// and convert it to a String which contains the xml tags
name|String
name|xml
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|payload
argument_list|)
decl_stmt|;
return|return
name|xml
return|;
block|}
block|}
end_class

end_unit

