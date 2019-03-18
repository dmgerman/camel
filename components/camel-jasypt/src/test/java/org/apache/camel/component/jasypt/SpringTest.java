begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jasypt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jasypt
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Value
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_class
annotation|@
name|Component
DECL|class|SpringTest
specifier|public
class|class
name|SpringTest
block|{
comment|// defaulted and overwritten in myproperties.properties
annotation|@
name|Value
argument_list|(
literal|"${available.prop:no Data}"
argument_list|)
DECL|field|data
specifier|private
name|String
name|data
init|=
literal|""
decl_stmt|;
annotation|@
name|Value
argument_list|(
literal|"${missing.prop:Spring}"
argument_list|)
DECL|field|defaulted
specifier|private
name|String
name|defaulted
decl_stmt|;
DECL|method|setData (String data)
specifier|public
name|void
name|setData
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
DECL|method|handleRequest (String request)
specifier|public
name|String
name|handleRequest
parameter_list|(
name|String
name|request
parameter_list|)
block|{
return|return
name|request
operator|+
literal|" "
operator|+
name|data
operator|+
literal|" "
operator|+
name|defaulted
return|;
block|}
block|}
end_class

end_unit

