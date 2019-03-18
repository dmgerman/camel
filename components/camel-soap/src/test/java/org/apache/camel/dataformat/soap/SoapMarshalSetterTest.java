begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.soap
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
package|;
end_package

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|GetCustomersByName
import|;
end_import

begin_comment
comment|/**  * Works like SoapMarshalTest but the data format is initialized by using the setters  * instead of the constructor  */
end_comment

begin_class
DECL|class|SoapMarshalSetterTest
specifier|public
class|class
name|SoapMarshalSetterTest
extends|extends
name|SoapMarshalTest
block|{
comment|/**      * Create Dataformat by using the setters      */
annotation|@
name|Override
DECL|method|createDataFormat ()
specifier|protected
name|SoapJaxbDataFormat
name|createDataFormat
parameter_list|()
block|{
name|String
name|jaxbPackage
init|=
name|GetCustomersByName
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|SoapJaxbDataFormat
name|dataFormat
init|=
operator|new
name|SoapJaxbDataFormat
argument_list|()
decl_stmt|;
name|dataFormat
operator|.
name|setContextPath
argument_list|(
name|jaxbPackage
argument_list|)
expr_stmt|;
name|dataFormat
operator|.
name|setElementNameStrategy
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|dataFormat
return|;
block|}
block|}
end_class

end_unit

