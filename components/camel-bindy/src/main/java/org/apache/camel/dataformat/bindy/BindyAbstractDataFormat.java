begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|DataFormat
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
name|spi
operator|.
name|PackageScanClassResolver
import|;
end_import

begin_class
DECL|class|BindyAbstractDataFormat
specifier|public
specifier|abstract
class|class
name|BindyAbstractDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
decl_stmt|;
DECL|field|locale
specifier|private
name|String
name|locale
decl_stmt|;
DECL|field|modelFactory
specifier|private
name|BindyAbstractFactory
name|modelFactory
decl_stmt|;
DECL|method|BindyAbstractDataFormat ()
specifier|public
name|BindyAbstractDataFormat
parameter_list|()
block|{     }
DECL|method|BindyAbstractDataFormat (String... packages)
specifier|public
name|BindyAbstractDataFormat
parameter_list|(
name|String
modifier|...
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|setPackages (String... packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
modifier|...
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|getLocale ()
specifier|public
name|String
name|getLocale
parameter_list|()
block|{
return|return
name|locale
return|;
block|}
DECL|method|setLocale (String locale)
specifier|public
name|void
name|setLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|this
operator|.
name|locale
operator|=
name|locale
expr_stmt|;
block|}
DECL|method|getFactory (PackageScanClassResolver resolver)
specifier|public
name|BindyAbstractFactory
name|getFactory
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|modelFactory
operator|==
literal|null
condition|)
block|{
name|modelFactory
operator|=
name|createModelFactory
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|modelFactory
operator|.
name|setLocale
argument_list|(
name|locale
argument_list|)
expr_stmt|;
block|}
return|return
name|modelFactory
return|;
block|}
DECL|method|setModelFactory (BindyAbstractFactory modelFactory)
specifier|public
name|void
name|setModelFactory
parameter_list|(
name|BindyAbstractFactory
name|modelFactory
parameter_list|)
block|{
name|this
operator|.
name|modelFactory
operator|=
name|modelFactory
expr_stmt|;
block|}
DECL|method|createModelFactory (PackageScanClassResolver resolver)
specifier|protected
specifier|abstract
name|BindyAbstractFactory
name|createModelFactory
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

