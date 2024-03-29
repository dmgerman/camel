begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelContext
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
name|Component
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
name|Endpoint
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
name|component
operator|.
name|file
operator|.
name|FileComponent
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
name|impl
operator|.
name|DefaultCamelContext
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
name|Registry
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
name|support
operator|.
name|DefaultRegistry
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|OsgiComponentResolverTest
specifier|public
class|class
name|OsgiComponentResolverTest
extends|extends
name|CamelOsgiTestSupport
block|{
annotation|@
name|Test
DECL|method|testOsgiResolverFindComponentTest ()
specifier|public
name|void
name|testOsgiResolverFindComponentTest
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|OsgiComponentResolver
name|resolver
init|=
operator|new
name|OsgiComponentResolver
argument_list|(
name|getBundleContext
argument_list|()
argument_list|)
decl_stmt|;
name|Component
name|component
init|=
name|resolver
operator|.
name|resolveComponent
argument_list|(
literal|"file_test"
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should find file_test component"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the file component here"
argument_list|,
name|component
operator|instanceof
name|FileComponent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOsgiResolverFindComponentFallbackTest ()
specifier|public
name|void
name|testOsgiResolverFindComponentFallbackTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Registry
name|registry
init|=
operator|new
name|DefaultRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"allstar-component"
argument_list|,
operator|new
name|SampleComponent
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|OsgiComponentResolver
name|resolver
init|=
operator|new
name|OsgiComponentResolver
argument_list|(
name|getBundleContext
argument_list|()
argument_list|)
decl_stmt|;
name|Component
name|component
init|=
name|resolver
operator|.
name|resolveComponent
argument_list|(
literal|"allstar"
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should find the super component"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the super component here"
argument_list|,
name|component
operator|instanceof
name|SampleComponent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOsgiResolverFindLanguageDoubleFallbackTest ()
specifier|public
name|void
name|testOsgiResolverFindLanguageDoubleFallbackTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Registry
name|registry
init|=
operator|new
name|DefaultRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"allstar"
argument_list|,
operator|new
name|SampleComponent
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"allstar-component"
argument_list|,
operator|new
name|SampleComponent
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|OsgiComponentResolver
name|resolver
init|=
operator|new
name|OsgiComponentResolver
argument_list|(
name|getBundleContext
argument_list|()
argument_list|)
decl_stmt|;
name|Component
name|component
init|=
name|resolver
operator|.
name|resolveComponent
argument_list|(
literal|"allstar"
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should find the super component"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the super component here"
argument_list|,
name|component
operator|instanceof
name|SampleComponent
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"We should NOT find the fallback component"
argument_list|,
operator|(
operator|(
name|SampleComponent
operator|)
name|component
operator|)
operator|.
name|isFallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|SampleComponent
specifier|private
specifier|static
class|class
name|SampleComponent
extends|extends
name|ServiceSupport
implements|implements
name|Component
block|{
DECL|field|fallback
specifier|private
name|boolean
name|fallback
decl_stmt|;
DECL|method|SampleComponent (boolean fallback)
name|SampleComponent
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri)
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, Map<String, Object> parameters)
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|useRawUri ()
specifier|public
name|boolean
name|useRawUri
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called"
argument_list|)
throw|;
block|}
DECL|method|isFallback ()
specifier|public
name|boolean
name|isFallback
parameter_list|()
block|{
return|return
name|fallback
return|;
block|}
DECL|method|setFallback (boolean fallback)
specifier|public
name|void
name|setFallback
parameter_list|(
name|boolean
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
block|}
end_class

end_unit

