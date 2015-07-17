begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
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
name|DefaultClassResolver
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
name|ClassResolver
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
name|FileUtil
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
name|IOHelper
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
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|PojoServiceRegistryFactoryImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|launch
operator|.
name|BundleDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|launch
operator|.
name|ClasspathScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|launch
operator|.
name|PojoServiceRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|connect
operator|.
name|launch
operator|.
name|PojoServiceRegistryFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
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
name|Bundle
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
name|osgi
operator|.
name|framework
operator|.
name|BundleException
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
name|Constants
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
name|Filter
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
name|FrameworkUtil
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
name|InvalidSyntaxException
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
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|TestSupport
operator|.
name|createDirectory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|TestSupport
operator|.
name|deleteDirectory
import|;
end_import

begin_comment
comment|/**  * Helper for using Blueprint with Camel.  */
end_comment

begin_class
DECL|class|CamelBlueprintHelper
specifier|public
specifier|final
class|class
name|CamelBlueprintHelper
block|{
DECL|field|DEFAULT_TIMEOUT
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_TIMEOUT
init|=
literal|30000
decl_stmt|;
DECL|field|BUNDLE_FILTER
specifier|public
specifier|static
specifier|final
name|String
name|BUNDLE_FILTER
init|=
literal|"(Bundle-SymbolicName=*)"
decl_stmt|;
DECL|field|BUNDLE_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|BUNDLE_VERSION
init|=
literal|"1.0.0"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelBlueprintHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|RESOLVER
specifier|private
specifier|static
specifier|final
name|ClassResolver
name|RESOLVER
init|=
operator|new
name|DefaultClassResolver
argument_list|()
decl_stmt|;
DECL|method|CamelBlueprintHelper ()
specifier|private
name|CamelBlueprintHelper
parameter_list|()
block|{     }
DECL|method|createBundleContext (String name, String descriptors, boolean includeTestBundle)
specifier|public
specifier|static
name|BundleContext
name|createBundleContext
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|descriptors
parameter_list|,
name|boolean
name|includeTestBundle
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createBundleContext
argument_list|(
name|name
argument_list|,
name|descriptors
argument_list|,
name|includeTestBundle
argument_list|,
name|BUNDLE_FILTER
argument_list|,
name|BUNDLE_VERSION
argument_list|)
return|;
block|}
DECL|method|createBundleContext (String name, String descriptors, boolean includeTestBundle, String bundleFilter, String testBundleVersion)
specifier|public
specifier|static
name|BundleContext
name|createBundleContext
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|descriptors
parameter_list|,
name|boolean
name|includeTestBundle
parameter_list|,
name|String
name|bundleFilter
parameter_list|,
name|String
name|testBundleVersion
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createBundleContext
argument_list|(
name|name
argument_list|,
name|descriptors
argument_list|,
name|includeTestBundle
argument_list|,
name|bundleFilter
argument_list|,
name|testBundleVersion
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createBundleContext (String name, String descriptors, boolean includeTestBundle, String bundleFilter, String testBundleVersion, String testBundleDirectives)
specifier|public
specifier|static
name|BundleContext
name|createBundleContext
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|descriptors
parameter_list|,
name|boolean
name|includeTestBundle
parameter_list|,
name|String
name|bundleFilter
parameter_list|,
name|String
name|testBundleVersion
parameter_list|,
name|String
name|testBundleDirectives
parameter_list|)
throws|throws
name|Exception
block|{
name|TinyBundle
name|bundle
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|includeTestBundle
condition|)
block|{
comment|// add ourselves as a bundle
name|bundle
operator|=
name|createTestBundle
argument_list|(
name|testBundleDirectives
operator|==
literal|null
condition|?
name|name
else|:
name|name
operator|+
literal|';'
operator|+
name|testBundleDirectives
argument_list|,
name|testBundleVersion
argument_list|,
name|descriptors
argument_list|)
expr_stmt|;
block|}
return|return
name|createBundleContext
argument_list|(
name|name
argument_list|,
name|bundleFilter
argument_list|,
name|bundle
argument_list|)
return|;
block|}
DECL|method|createBundleContext (String name, String bundleFilter, TinyBundle bundle)
specifier|public
specifier|static
name|BundleContext
name|createBundleContext
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|bundleFilter
parameter_list|,
name|TinyBundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ensure felix-connect stores bundles in an unique target directory
name|String
name|uid
init|=
literal|""
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|String
name|tempDir
init|=
literal|"target/bundles/"
operator|+
name|uid
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.osgi.framework.storage"
argument_list|,
name|tempDir
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
name|tempDir
argument_list|)
expr_stmt|;
comment|// use another directory for the jar of the bundle as it cannot be in the same directory
comment|// as it has a file lock during running the tests which will cause the temp dir to not be
comment|// fully deleted between tests
name|createDirectory
argument_list|(
literal|"target/test-bundles"
argument_list|)
expr_stmt|;
comment|// get the bundles
name|List
argument_list|<
name|BundleDescriptor
argument_list|>
name|bundles
init|=
name|getBundleDescriptors
argument_list|(
name|bundleFilter
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
name|String
name|jarName
init|=
name|name
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|+
literal|"-"
operator|+
name|uid
operator|+
literal|".jar"
decl_stmt|;
name|bundles
operator|.
name|add
argument_list|(
name|getBundleDescriptor
argument_list|(
literal|"target/test-bundles/"
operator|+
name|jarName
argument_list|,
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bundles
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|BundleDescriptor
name|desc
init|=
name|bundles
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Bundle #{} -> {}"
argument_list|,
name|i
argument_list|,
name|desc
argument_list|)
expr_stmt|;
block|}
block|}
comment|// setup felix-connect to use our bundles
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|PojoServiceRegistryFactory
operator|.
name|BUNDLE_DESCRIPTORS
argument_list|,
name|bundles
argument_list|)
expr_stmt|;
comment|// create pojorsr osgi service registry
name|PojoServiceRegistry
name|reg
init|=
operator|new
name|PojoServiceRegistryFactoryImpl
argument_list|()
operator|.
name|newPojoServiceRegistry
argument_list|(
name|config
argument_list|)
decl_stmt|;
return|return
name|reg
operator|.
name|getBundleContext
argument_list|()
return|;
block|}
DECL|method|disposeBundleContext (BundleContext bundleContext)
specifier|public
specifier|static
name|void
name|disposeBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|BundleException
block|{
try|try
block|{
if|if
condition|(
name|bundleContext
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Bundle
argument_list|>
name|bundles
init|=
operator|new
name|ArrayList
argument_list|<
name|Bundle
argument_list|>
argument_list|()
decl_stmt|;
name|bundles
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|bundleContext
operator|.
name|getBundles
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|bundles
argument_list|)
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|bundles
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping bundle {}"
argument_list|,
name|bundle
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IllegalStateException
name|ise
init|=
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|ise
operator|!=
literal|null
condition|)
block|{
comment|// we dont care about illegal state exception as that may happen from OSGi
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error during disposing BundleContext. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error during disposing BundleContext. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|String
name|tempDir
init|=
name|System
operator|.
name|clearProperty
argument_list|(
literal|"org.osgi.framework.storage"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tempDir
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Deleting work directory {}"
argument_list|,
name|tempDir
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
name|tempDir
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// pick up persistent file configuration
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|setPersistentFileForConfigAdmin (BundleContext bundleContext, String pid, String fileName, Dictionary props)
specifier|public
specifier|static
name|void
name|setPersistentFileForConfigAdmin
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|String
name|pid
parameter_list|,
name|String
name|fileName
parameter_list|,
name|Dictionary
name|props
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|pid
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|fileName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The persistent file should not be null"
argument_list|)
throw|;
block|}
else|else
block|{
name|File
name|load
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading properties from OSGi config admin file: {}"
argument_list|,
name|load
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|Properties
name|cfg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|utils
operator|.
name|properties
operator|.
name|Properties
argument_list|(
name|load
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|cfg
operator|.
name|keySet
argument_list|()
control|)
block|{
name|props
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|cfg
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ConfigurationAdmin
name|configAdmin
init|=
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|configAdmin
operator|!=
literal|null
condition|)
block|{
comment|// ensure we update
comment|// we *have to* use "null" as 2nd arg to have correct bundle location for Configuration object
name|Configuration
name|config
init|=
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
name|pid
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Updating ConfigAdmin {} by overriding properties {}"
argument_list|,
name|config
argument_list|,
name|props
argument_list|)
expr_stmt|;
name|config
operator|.
name|update
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getOsgiService (BundleContext bundleContext, Class<T> type, long timeout)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|,
literal|null
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|getOsgiService (BundleContext bundleContext, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|,
literal|null
argument_list|,
name|DEFAULT_TIMEOUT
argument_list|)
return|;
block|}
DECL|method|getOsgiService (BundleContext bundleContext, Class<T> type, String filter)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
return|return
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|type
argument_list|,
name|filter
argument_list|,
name|DEFAULT_TIMEOUT
argument_list|)
return|;
block|}
DECL|method|getOsgiService (BundleContext bundleContext, Class<T> type, String filter, long timeout)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|filter
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|ServiceTracker
name|tracker
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|flt
decl_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|filter
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
operator|+
name|filter
operator|+
literal|")"
expr_stmt|;
block|}
else|else
block|{
name|flt
operator|=
literal|"(&("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")("
operator|+
name|filter
operator|+
literal|"))"
expr_stmt|;
block|}
block|}
else|else
block|{
name|flt
operator|=
literal|"("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"="
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
name|Filter
name|osgiFilter
init|=
name|FrameworkUtil
operator|.
name|createFilter
argument_list|(
name|flt
argument_list|)
decl_stmt|;
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|(
name|bundleContext
argument_list|,
name|osgiFilter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tracker
operator|.
name|open
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Note that the tracker is not closed to keep the reference
comment|// This is buggy, as the service reference may change i think
name|Object
name|svc
init|=
name|tracker
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
if|if
condition|(
name|svc
operator|==
literal|null
condition|)
block|{
name|Dictionary
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|dic
init|=
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Test bundle headers: "
operator|+
name|explode
argument_list|(
name|dic
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
control|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"ServiceReference: "
operator|+
name|ref
operator|+
literal|", bundle: "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
operator|+
literal|", symbolicName: "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ServiceReference
name|ref
range|:
name|asCollection
argument_list|(
name|bundleContext
operator|.
name|getAllServiceReferences
argument_list|(
literal|null
argument_list|,
name|flt
argument_list|)
argument_list|)
control|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Filtered ServiceReference: "
operator|+
name|ref
operator|+
literal|", bundle: "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
operator|+
literal|", symbolicName: "
operator|+
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Gave up waiting for service "
operator|+
name|flt
argument_list|)
throw|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|svc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid filter"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createTestBundle (String name, String version, String descriptors)
specifier|protected
specifier|static
name|TinyBundle
name|createTestBundle
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|descriptors
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|MalformedURLException
block|{
name|TinyBundle
name|bundle
init|=
name|TinyBundles
operator|.
name|newBundle
argument_list|()
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
name|getBlueprintDescriptors
argument_list|(
name|descriptors
argument_list|)
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using Blueprint XML file: "
operator|+
name|url
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/blueprint-"
operator|+
name|url
operator|.
name|getFile
argument_list|()
operator|.
name|replace
argument_list|(
literal|"/"
argument_list|,
literal|"-"
argument_list|)
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
name|bundle
operator|.
name|set
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-ManifestVersion"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-SymbolicName"
argument_list|,
name|name
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-Version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
return|return
name|bundle
return|;
block|}
comment|/**      * Explode the dictionary into a<code>,</code> delimited list of<code>key=value</code> pairs.      */
DECL|method|explode (Dictionary<?, ?> dictionary)
specifier|private
specifier|static
name|String
name|explode
parameter_list|(
name|Dictionary
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|dictionary
parameter_list|)
block|{
name|Enumeration
argument_list|<
name|?
argument_list|>
name|keys
init|=
name|dictionary
operator|.
name|keys
argument_list|()
decl_stmt|;
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|key
init|=
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s=%s"
argument_list|,
name|key
argument_list|,
name|dictionary
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Provides an iterable collection of references, even if the original array is<code>null</code>.      */
DECL|method|asCollection (ServiceReference[] references)
specifier|private
specifier|static
name|Collection
argument_list|<
name|ServiceReference
argument_list|>
name|asCollection
parameter_list|(
name|ServiceReference
index|[]
name|references
parameter_list|)
block|{
return|return
name|references
operator|==
literal|null
condition|?
operator|new
name|ArrayList
argument_list|<
name|ServiceReference
argument_list|>
argument_list|(
literal|0
argument_list|)
else|:
name|Arrays
operator|.
name|asList
argument_list|(
name|references
argument_list|)
return|;
block|}
comment|/**      * Gets list of bundle descriptors.      * @param bundleFilter Filter expression for OSGI bundles.      *      * @return List pointers to OSGi bundles.      * @throws Exception If looking up the bundles fails.      */
DECL|method|getBundleDescriptors (final String bundleFilter)
specifier|private
specifier|static
name|List
argument_list|<
name|BundleDescriptor
argument_list|>
name|getBundleDescriptors
parameter_list|(
specifier|final
name|String
name|bundleFilter
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ClasspathScanner
argument_list|()
operator|.
name|scanForBundles
argument_list|(
name|bundleFilter
argument_list|)
return|;
block|}
comment|/**      * Gets the bundle descriptors as {@link URL} resources.      *      * @param descriptors the bundle descriptors, can be separated by comma      * @return the bundle descriptors.      * @throws FileNotFoundException is thrown if a bundle descriptor cannot be found      */
DECL|method|getBlueprintDescriptors (String descriptors)
specifier|private
specifier|static
name|Collection
argument_list|<
name|URL
argument_list|>
name|getBlueprintDescriptors
parameter_list|(
name|String
name|descriptors
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|MalformedURLException
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|descriptor
init|=
name|descriptors
decl_stmt|;
if|if
condition|(
name|descriptor
operator|!=
literal|null
condition|)
block|{
comment|// there may be more resources separated by comma
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|descriptor
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resource descriptor: {}"
argument_list|,
name|s
argument_list|)
expr_stmt|;
comment|// remove leading / to be able to load resource from the classpath
name|s
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|s
argument_list|)
expr_stmt|;
comment|// if there is wildcards for *.xml then we need to find the urls from the package
if|if
condition|(
name|s
operator|.
name|endsWith
argument_list|(
literal|"*.xml"
argument_list|)
condition|)
block|{
name|String
name|packageName
init|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|length
argument_list|()
operator|-
literal|5
argument_list|)
decl_stmt|;
comment|// remove trailing / to be able to load resource from the classpath
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|ObjectHelper
operator|.
name|loadResourcesAsURL
argument_list|(
name|packageName
argument_list|)
decl_stmt|;
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|urls
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
operator|&&
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|packageName
operator|+
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Resolving resource: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|URL
name|xmlUrl
init|=
name|ObjectHelper
operator|.
name|loadResourceAsURL
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlUrl
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|xmlUrl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Resolving resource: {}"
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|RESOLVER
argument_list|,
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Resource "
operator|+
name|s
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No bundle descriptor configured. Override getBlueprintDescriptor() or getBlueprintDescriptors() method"
argument_list|)
throw|;
block|}
if|if
condition|(
name|answer
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find any resources in classpath from descriptor "
operator|+
name|descriptors
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getBundleDescriptor (String path, TinyBundle bundle)
specifier|private
specifier|static
name|BundleDescriptor
name|getBundleDescriptor
parameter_list|(
name|String
name|path
parameter_list|,
name|TinyBundle
name|bundle
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// tell the JVM its okay to delete this file on exit as its a temporary file
comment|// the JVM may not successfully delete the file though
name|file
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|bundle
operator|.
name|build
argument_list|()
decl_stmt|;
try|try
block|{
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|fos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
name|fos
argument_list|)
expr_stmt|;
block|}
name|BundleDescriptor
name|answer
init|=
literal|null
decl_stmt|;
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
name|JarInputStream
name|jis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|jis
operator|=
operator|new
name|JarInputStream
argument_list|(
name|fis
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|jis
operator|.
name|getManifest
argument_list|()
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
operator|new
name|BundleDescriptor
argument_list|(
name|bundle
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
literal|"jar:"
operator|+
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"!/"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|jis
argument_list|,
name|fis
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

