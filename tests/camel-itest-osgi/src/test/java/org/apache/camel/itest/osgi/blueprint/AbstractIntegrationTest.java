begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|blueprint
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationTestSupport
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
name|exam
operator|.
name|CoreOptions
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
name|exam
operator|.
name|Inject
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
name|exam
operator|.
name|options
operator|.
name|MavenArtifactProvisionOption
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
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_class
DECL|class|AbstractIntegrationTest
specifier|public
specifier|abstract
class|class
name|AbstractIntegrationTest
extends|extends
name|OSGiIntegrationTestSupport
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
annotation|@
name|Inject
DECL|field|bundleContext
specifier|protected
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|method|getOsgiService (Class<T> type, long timeout)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
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
name|type
argument_list|,
literal|null
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|getOsgiService (Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
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
name|type
argument_list|,
literal|null
argument_list|,
name|DEFAULT_TIMEOUT
argument_list|)
return|;
block|}
DECL|method|getOsgiService (Class<T> type, String filter, long timeout)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOsgiService
parameter_list|(
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
name|type
operator|.
name|cast
argument_list|(
name|tracker
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
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
name|System
operator|.
name|err
operator|.
name|println
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"ServiceReference: "
operator|+
name|ref
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Filtered ServiceReference: "
operator|+
name|ref
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
DECL|method|installBundle (String groupId, String artifactId)
specifier|protected
name|Bundle
name|installBundle
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
throws|throws
name|Exception
block|{
name|MavenArtifactProvisionOption
name|mvnUrl
init|=
name|mavenBundle
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
decl_stmt|;
return|return
name|bundleContext
operator|.
name|installBundle
argument_list|(
name|mvnUrl
operator|.
name|getURL
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getInstalledBundle (String symbolicName)
specifier|protected
name|Bundle
name|getInstalledBundle
parameter_list|(
name|String
name|symbolicName
parameter_list|)
block|{
for|for
control|(
name|Bundle
name|b
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
name|b
operator|.
name|getSymbolicName
argument_list|()
operator|.
name|equals
argument_list|(
name|symbolicName
argument_list|)
condition|)
block|{
return|return
name|b
return|;
block|}
block|}
for|for
control|(
name|Bundle
name|b
range|:
name|bundleContext
operator|.
name|getBundles
argument_list|()
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Bundle: "
operator|+
name|b
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
literal|"Bundle "
operator|+
name|symbolicName
operator|+
literal|" does not exist"
argument_list|)
throw|;
block|}
comment|/*      * Explode the dictionary into a ,-delimited list of key=value pairs      */
DECL|method|explode (Dictionary dictionary)
specifier|private
specifier|static
name|String
name|explode
parameter_list|(
name|Dictionary
name|dictionary
parameter_list|)
block|{
name|Enumeration
name|keys
init|=
name|dictionary
operator|.
name|keys
argument_list|()
decl_stmt|;
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
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
comment|/*      * Provides an iterable collection of references, even if the original array is null      */
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
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|asList
argument_list|(
name|references
argument_list|)
else|:
name|Collections
operator|.
expr|<
name|ServiceReference
operator|>
name|emptyList
argument_list|()
return|;
block|}
comment|/**      *  Create an provisioning option for the specified maven artifact      * (groupId and artifactId), using the version found in the list      * of dependencies of this maven project.      *      * @param groupId the groupId of the maven bundle      * @param artifactId the artifactId of the maven bundle      * @return the provisioning option for the given bundle      */
DECL|method|mavenBundle (String groupId, String artifactId)
specifier|protected
specifier|static
name|MavenArtifactProvisionOption
name|mavenBundle
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
block|{
return|return
name|CoreOptions
operator|.
name|mavenBundle
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
operator|.
name|versionAsInProject
argument_list|()
return|;
block|}
comment|/**      *  Create an provisioning option for the specified maven artifact      * (groupId and artifactId), using the version found in the list      * of dependencies of this maven project.      *      * @param groupId the groupId of the maven bundle      * @param artifactId the artifactId of the maven bundle      * @param version the version of the maven bundle      * @return the provisioning option for the given bundle      */
DECL|method|mavenBundle (String groupId, String artifactId, String version)
specifier|protected
specifier|static
name|MavenArtifactProvisionOption
name|mavenBundle
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
name|CoreOptions
operator|.
name|mavenBundle
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
operator|.
name|version
argument_list|(
name|version
argument_list|)
return|;
block|}
block|}
end_class

end_unit

