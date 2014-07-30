begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
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
name|ClassResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|mock
operator|.
name|MockBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|mock
operator|.
name|MockBundleContext
import|;
end_import

begin_class
DECL|class|CamelOsgiTestSupport
specifier|public
class|class
name|CamelOsgiTestSupport
extends|extends
name|Assert
block|{
DECL|field|bundle
specifier|private
name|MockBundle
name|bundle
init|=
operator|new
name|CamelMockBundle
argument_list|()
decl_stmt|;
DECL|field|bundleContext
specifier|private
name|MockBundleContext
name|bundleContext
init|=
operator|new
name|CamelMockBundleContext
argument_list|(
name|bundle
argument_list|)
decl_stmt|;
DECL|field|packageScanClassResolver
specifier|private
name|OsgiPackageScanClassResolver
name|packageScanClassResolver
init|=
operator|new
name|OsgiPackageScanClassResolver
argument_list|(
name|bundleContext
argument_list|)
decl_stmt|;
DECL|field|classResolver
specifier|private
name|ClassResolver
name|classResolver
init|=
operator|new
name|OsgiClassResolver
argument_list|(
literal|null
argument_list|,
name|bundleContext
argument_list|)
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|bundleContext
operator|.
name|setBundle
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|getBundleContext ()
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
DECL|method|getPackageScanClassResolver ()
specifier|public
name|OsgiPackageScanClassResolver
name|getPackageScanClassResolver
parameter_list|()
block|{
return|return
name|packageScanClassResolver
return|;
block|}
DECL|method|getClassResolver ()
specifier|public
name|ClassResolver
name|getClassResolver
parameter_list|()
block|{
return|return
name|classResolver
return|;
block|}
block|}
end_class

end_unit

