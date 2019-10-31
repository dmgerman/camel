begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
package|;
end_package

begin_comment
comment|/**  * Global configuration for Camel Main to setup context name, stream caching and other global configurations.  */
end_comment

begin_class
DECL|class|MainConfigurationProperties
specifier|public
class|class
name|MainConfigurationProperties
extends|extends
name|DefaultConfigurationProperties
argument_list|<
name|MainConfigurationProperties
argument_list|>
block|{
DECL|field|autoConfigurationEnabled
specifier|private
name|boolean
name|autoConfigurationEnabled
init|=
literal|true
decl_stmt|;
DECL|field|autoConfigurationEnvironmentVariablesEnabled
specifier|private
name|boolean
name|autoConfigurationEnvironmentVariablesEnabled
init|=
literal|true
decl_stmt|;
DECL|field|autoConfigurationFailFast
specifier|private
name|boolean
name|autoConfigurationFailFast
init|=
literal|true
decl_stmt|;
DECL|field|autoConfigurationLogSummary
specifier|private
name|boolean
name|autoConfigurationLogSummary
init|=
literal|true
decl_stmt|;
DECL|field|autowireComponentProperties
specifier|private
name|boolean
name|autowireComponentProperties
init|=
literal|true
decl_stmt|;
DECL|field|autowireComponentPropertiesDeep
specifier|private
name|boolean
name|autowireComponentPropertiesDeep
decl_stmt|;
DECL|field|autowireComponentPropertiesNonNullOnly
specifier|private
name|boolean
name|autowireComponentPropertiesNonNullOnly
decl_stmt|;
DECL|field|autowireComponentPropertiesAllowPrivateSetter
specifier|private
name|boolean
name|autowireComponentPropertiesAllowPrivateSetter
init|=
literal|true
decl_stmt|;
DECL|field|durationHitExitCode
specifier|private
name|int
name|durationHitExitCode
decl_stmt|;
DECL|field|hangupInterceptorEnabled
specifier|private
name|boolean
name|hangupInterceptorEnabled
init|=
literal|true
decl_stmt|;
DECL|field|packageScanRouteBuilders
specifier|private
name|String
name|packageScanRouteBuilders
decl_stmt|;
comment|// extended configuration
DECL|field|hystrixConfigurationProperties
specifier|private
specifier|final
name|HystrixConfigurationProperties
name|hystrixConfigurationProperties
init|=
operator|new
name|HystrixConfigurationProperties
argument_list|(
name|this
argument_list|)
decl_stmt|;
DECL|field|restConfigurationProperties
specifier|private
specifier|final
name|RestConfigurationProperties
name|restConfigurationProperties
init|=
operator|new
name|RestConfigurationProperties
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|// extended
comment|// --------------------------------------------------------------
comment|/**      * To configure Hystrix EIP      */
DECL|method|hystrix ()
specifier|public
name|HystrixConfigurationProperties
name|hystrix
parameter_list|()
block|{
return|return
name|hystrixConfigurationProperties
return|;
block|}
comment|/**      * To configure Rest DSL      */
DECL|method|rest ()
specifier|public
name|RestConfigurationProperties
name|rest
parameter_list|()
block|{
return|return
name|restConfigurationProperties
return|;
block|}
comment|// getter and setters
comment|// --------------------------------------------------------------
DECL|method|isAutoConfigurationEnabled ()
specifier|public
name|boolean
name|isAutoConfigurationEnabled
parameter_list|()
block|{
return|return
name|autoConfigurationEnabled
return|;
block|}
comment|/**      * Whether auto configuration of components, dataformats, languages is enabled or not.      * When enabled the configuration parameters are loaded from the properties component      * and optionally from the classpath file META-INF/services/org/apache/camel/autowire.properties.      * You can prefix the parameters in the properties file with:      * - camel.component.name.option1=value1      * - camel.component.name.option2=value2      * - camel.dataformat.name.option1=value1      * - camel.dataformat.name.option2=value2      * - camel.language.name.option1=value1      * - camel.language.name.option2=value2      * Where name is the name of the component, dataformat or language such as seda,direct,jaxb.      *<p/>      * The auto configuration also works for any options on components      * that is a complex type (not standard Java type) and there has been an explicit single      * bean instance registered to the Camel registry via the {@link org.apache.camel.spi.Registry#bind(String, Object)} method      * or by using the {@link org.apache.camel.BindToRegistry} annotation style.      *<p/>      * This option is default enabled.      */
DECL|method|setAutoConfigurationEnabled (boolean autoConfigurationEnabled)
specifier|public
name|void
name|setAutoConfigurationEnabled
parameter_list|(
name|boolean
name|autoConfigurationEnabled
parameter_list|)
block|{
name|this
operator|.
name|autoConfigurationEnabled
operator|=
name|autoConfigurationEnabled
expr_stmt|;
block|}
DECL|method|isAutoConfigurationEnvironmentVariablesEnabled ()
specifier|public
name|boolean
name|isAutoConfigurationEnvironmentVariablesEnabled
parameter_list|()
block|{
return|return
name|autoConfigurationEnvironmentVariablesEnabled
return|;
block|}
comment|/**      * Whether auto configuration should include OS environment variables as well. When enabled this      * allows to overrule any configuration using an OS environment variable. For example to set      * a shutdown timeout of 5 seconds: CAMEL_MAIN_SHUTDOWNTIMEOUT=5.      *<p/>      * This option is default enabled.      */
DECL|method|setAutoConfigurationEnvironmentVariablesEnabled (boolean autoConfigurationEnvironmentVariablesEnabled)
specifier|public
name|void
name|setAutoConfigurationEnvironmentVariablesEnabled
parameter_list|(
name|boolean
name|autoConfigurationEnvironmentVariablesEnabled
parameter_list|)
block|{
name|this
operator|.
name|autoConfigurationEnvironmentVariablesEnabled
operator|=
name|autoConfigurationEnvironmentVariablesEnabled
expr_stmt|;
block|}
DECL|method|isAutoConfigurationFailFast ()
specifier|public
name|boolean
name|isAutoConfigurationFailFast
parameter_list|()
block|{
return|return
name|autoConfigurationFailFast
return|;
block|}
comment|/**      * Whether auto configuration should fail fast when configuring one ore more properties fails for whatever reason      * such as a invalid property name, etc.      *<p/>      * This option is default enabled.      */
DECL|method|setAutoConfigurationFailFast (boolean autoConfigurationFailFast)
specifier|public
name|void
name|setAutoConfigurationFailFast
parameter_list|(
name|boolean
name|autoConfigurationFailFast
parameter_list|)
block|{
name|this
operator|.
name|autoConfigurationFailFast
operator|=
name|autoConfigurationFailFast
expr_stmt|;
block|}
DECL|method|isAutoConfigurationLogSummary ()
specifier|public
name|boolean
name|isAutoConfigurationLogSummary
parameter_list|()
block|{
return|return
name|autoConfigurationLogSummary
return|;
block|}
comment|/**      * Whether auto configuration should log a summary with the configured properties.      *<p/>      * This option is default enabled.      */
DECL|method|setAutoConfigurationLogSummary (boolean autoConfigurationLogSummary)
specifier|public
name|void
name|setAutoConfigurationLogSummary
parameter_list|(
name|boolean
name|autoConfigurationLogSummary
parameter_list|)
block|{
name|this
operator|.
name|autoConfigurationLogSummary
operator|=
name|autoConfigurationLogSummary
expr_stmt|;
block|}
DECL|method|isAutowireComponentProperties ()
specifier|public
name|boolean
name|isAutowireComponentProperties
parameter_list|()
block|{
return|return
name|autowireComponentProperties
return|;
block|}
comment|/**      * Whether autowiring components with properties that are of same type, which has been added to the Camel registry, as a singleton instance.      * This is used for convention over configuration to inject DataSource, AmazonLogin instances to the components.      *<p/>      * This option is default enabled.      */
DECL|method|setAutowireComponentProperties (boolean autowireComponentProperties)
specifier|public
name|void
name|setAutowireComponentProperties
parameter_list|(
name|boolean
name|autowireComponentProperties
parameter_list|)
block|{
name|this
operator|.
name|autowireComponentProperties
operator|=
name|autowireComponentProperties
expr_stmt|;
block|}
DECL|method|isAutowireComponentPropertiesDeep ()
specifier|public
name|boolean
name|isAutowireComponentPropertiesDeep
parameter_list|()
block|{
return|return
name|autowireComponentPropertiesDeep
return|;
block|}
comment|/**      * Whether autowiring components (with deep nesting by attempting to walk as deep down the object graph by creating new empty objects on the way if needed)      * with properties that are of same type, which has been added to the Camel registry, as a singleton instance.      * This is used for convention over configuration to inject DataSource, AmazonLogin instances to the components.      *<p/>      * This option is default disabled.      */
DECL|method|setAutowireComponentPropertiesDeep (boolean autowireComponentPropertiesDeep)
specifier|public
name|void
name|setAutowireComponentPropertiesDeep
parameter_list|(
name|boolean
name|autowireComponentPropertiesDeep
parameter_list|)
block|{
name|this
operator|.
name|autowireComponentPropertiesDeep
operator|=
name|autowireComponentPropertiesDeep
expr_stmt|;
block|}
DECL|method|isAutowireComponentPropertiesNonNullOnly ()
specifier|public
name|boolean
name|isAutowireComponentPropertiesNonNullOnly
parameter_list|()
block|{
return|return
name|autowireComponentPropertiesNonNullOnly
return|;
block|}
comment|/**      * Whether to only autowire if the property has no default value or has not been configured explicit.      *<p/>      * This option is default disabled.      */
DECL|method|setAutowireComponentPropertiesNonNullOnly (boolean autowireComponentPropertiesNonNullOnly)
specifier|public
name|void
name|setAutowireComponentPropertiesNonNullOnly
parameter_list|(
name|boolean
name|autowireComponentPropertiesNonNullOnly
parameter_list|)
block|{
name|this
operator|.
name|autowireComponentPropertiesNonNullOnly
operator|=
name|autowireComponentPropertiesNonNullOnly
expr_stmt|;
block|}
DECL|method|isAutowireComponentPropertiesAllowPrivateSetter ()
specifier|public
name|boolean
name|isAutowireComponentPropertiesAllowPrivateSetter
parameter_list|()
block|{
return|return
name|autowireComponentPropertiesAllowPrivateSetter
return|;
block|}
comment|/**      * Whether autowiring components allows to use private setter method when setting the value. This may be needed      * in some rare situations when some configuration classes may configure via constructors over setters. But      * constructor configuration is more cumbersome to use via .properties files etc.      */
DECL|method|setAutowireComponentPropertiesAllowPrivateSetter (boolean autowireComponentPropertiesAllowPrivateSetter)
specifier|public
name|void
name|setAutowireComponentPropertiesAllowPrivateSetter
parameter_list|(
name|boolean
name|autowireComponentPropertiesAllowPrivateSetter
parameter_list|)
block|{
name|this
operator|.
name|autowireComponentPropertiesAllowPrivateSetter
operator|=
name|autowireComponentPropertiesAllowPrivateSetter
expr_stmt|;
block|}
DECL|method|isHangupInterceptorEnabled ()
specifier|public
name|boolean
name|isHangupInterceptorEnabled
parameter_list|()
block|{
return|return
name|hangupInterceptorEnabled
return|;
block|}
comment|/**      * Whether to use graceful hangup when Camel is stopping or when the JVM terminates.      */
DECL|method|setHangupInterceptorEnabled (boolean hangupInterceptorEnabled)
specifier|public
name|void
name|setHangupInterceptorEnabled
parameter_list|(
name|boolean
name|hangupInterceptorEnabled
parameter_list|)
block|{
name|this
operator|.
name|hangupInterceptorEnabled
operator|=
name|hangupInterceptorEnabled
expr_stmt|;
block|}
DECL|method|getPackageScanRouteBuilders ()
specifier|public
name|String
name|getPackageScanRouteBuilders
parameter_list|()
block|{
return|return
name|packageScanRouteBuilders
return|;
block|}
comment|/**      * Sets package names for scanning for {@link org.apache.camel.builder.RouteBuilder} classes as candidates to be included.      * If you are using Spring Boot then its instead recommended to use Spring Boots component scanning and annotate your route builder      * classes with `@Component`. In other words only use this for Camel Main in standalone mode.      */
DECL|method|setPackageScanRouteBuilders (String packageScanRouteBuilders)
specifier|public
name|void
name|setPackageScanRouteBuilders
parameter_list|(
name|String
name|packageScanRouteBuilders
parameter_list|)
block|{
name|this
operator|.
name|packageScanRouteBuilders
operator|=
name|packageScanRouteBuilders
expr_stmt|;
block|}
DECL|method|getDurationHitExitCode ()
specifier|public
name|int
name|getDurationHitExitCode
parameter_list|()
block|{
return|return
name|durationHitExitCode
return|;
block|}
comment|/**      * Sets the exit code for the application if duration was hit      */
DECL|method|setDurationHitExitCode (int durationHitExitCode)
specifier|public
name|void
name|setDurationHitExitCode
parameter_list|(
name|int
name|durationHitExitCode
parameter_list|)
block|{
name|this
operator|.
name|durationHitExitCode
operator|=
name|durationHitExitCode
expr_stmt|;
block|}
comment|// fluent builders
comment|// --------------------------------------------------------------
comment|/**      * Whether auto configuration of components/dataformats/languages is enabled or not.      * When enabled the configuration parameters are loaded from the properties component      * and configured as defaults (similar to spring-boot auto-configuration). You can prefix      * the parameters in the properties file with:      * - camel.component.name.option1=value1      * - camel.component.name.option2=value2      * - camel.dataformat.name.option1=value1      * - camel.dataformat.name.option2=value2      * - camel.language.name.option1=value1      * - camel.language.name.option2=value2      * Where name is the name of the component, dataformat or language such as seda,direct,jaxb.      *<p/>      * The auto configuration also works for any options on components      * that is a complex type (not standard Java type) and there has been an explicit single      * bean instance registered to the Camel registry via the {@link org.apache.camel.spi.Registry#bind(String, Object)} method      * or by using the {@link org.apache.camel.BindToRegistry} annotation style.      *<p/>      * This option is default enabled.      */
DECL|method|withAutoConfigurationEnabled (boolean autoConfigurationEnabled)
specifier|public
name|MainConfigurationProperties
name|withAutoConfigurationEnabled
parameter_list|(
name|boolean
name|autoConfigurationEnabled
parameter_list|)
block|{
name|this
operator|.
name|autoConfigurationEnabled
operator|=
name|autoConfigurationEnabled
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether auto configuration should include OS environment variables as well. When enabled this      * allows to overrule any configuration using an OS environment variable. For example to set      * a shutdown timeout of 5 seconds: CAMEL_MAIN_SHUTDOWNTIMEOUT=5.      *<p/>      * This option is default enabled.      */
DECL|method|withAutoConfigurationEnvironmentVariablesEnabled (boolean autoConfigurationEnvironmentVariablesEnabled)
specifier|public
name|MainConfigurationProperties
name|withAutoConfigurationEnvironmentVariablesEnabled
parameter_list|(
name|boolean
name|autoConfigurationEnvironmentVariablesEnabled
parameter_list|)
block|{
name|this
operator|.
name|autoConfigurationEnvironmentVariablesEnabled
operator|=
name|autoConfigurationEnvironmentVariablesEnabled
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether auto configuration should fail fast when configuring one ore more properties fails for whatever reason      * such as a invalid property name, etc.      *<p/>      * This option is default enabled.      */
DECL|method|withAutoConfigurationFailFast (boolean autoConfigurationFailFast)
specifier|public
name|MainConfigurationProperties
name|withAutoConfigurationFailFast
parameter_list|(
name|boolean
name|autoConfigurationFailFast
parameter_list|)
block|{
name|this
operator|.
name|autoConfigurationFailFast
operator|=
name|autoConfigurationFailFast
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether auto configuration should log a summary with the configured properties.      *<p/>      * This option is default enabled.      */
DECL|method|withAutoConfigurationLogSummary (boolean autoConfigurationLogSummary)
specifier|public
name|MainConfigurationProperties
name|withAutoConfigurationLogSummary
parameter_list|(
name|boolean
name|autoConfigurationLogSummary
parameter_list|)
block|{
name|this
operator|.
name|autoConfigurationLogSummary
operator|=
name|autoConfigurationLogSummary
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether autowiring components with properties that are of same type, which has been added to the Camel registry, as a singleton instance.      * This is used for convention over configuration to inject DataSource, AmazonLogin instances to the components.      *<p/>      * This option is default enabled.      */
DECL|method|withAutowireComponentProperties (boolean autowireComponentProperties)
specifier|public
name|MainConfigurationProperties
name|withAutowireComponentProperties
parameter_list|(
name|boolean
name|autowireComponentProperties
parameter_list|)
block|{
name|this
operator|.
name|autowireComponentProperties
operator|=
name|autowireComponentProperties
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether autowiring components (with deep nesting by attempting to walk as deep down the object graph by creating new empty objects on the way if needed)      * with properties that are of same type, which has been added to the Camel registry, as a singleton instance.      * This is used for convention over configuration to inject DataSource, AmazonLogin instances to the components.      *<p/>      * This option is default disabled.      */
DECL|method|withAutowireComponentPropertiesDeep (boolean autowireComponentPropertiesDeep)
specifier|public
name|MainConfigurationProperties
name|withAutowireComponentPropertiesDeep
parameter_list|(
name|boolean
name|autowireComponentPropertiesDeep
parameter_list|)
block|{
name|this
operator|.
name|autowireComponentPropertiesDeep
operator|=
name|autowireComponentPropertiesDeep
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to only autowire if the property has no default value or has not been configured explicit.      *<p/>      * This option is default disabled.      */
DECL|method|withAutowireComponentPropertiesNonNullOnly (boolean autowireComponentPropertiesNonNullOnly)
specifier|public
name|MainConfigurationProperties
name|withAutowireComponentPropertiesNonNullOnly
parameter_list|(
name|boolean
name|autowireComponentPropertiesNonNullOnly
parameter_list|)
block|{
name|this
operator|.
name|autowireComponentPropertiesNonNullOnly
operator|=
name|autowireComponentPropertiesNonNullOnly
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether autowiring components (with deep nesting by attempting to walk as deep down the object graph by creating new empty objects on the way if needed)      * with properties that are of same type, which has been added to the Camel registry, as a singleton instance.      * This is used for convention over configuration to inject DataSource, AmazonLogin instances to the components.      *<p/>      * This option is default enabled.      */
DECL|method|withAutowireComponentPropertiesAllowPrivateSetter (boolean autowireComponentPropertiesAllowPrivateSetter)
specifier|public
name|MainConfigurationProperties
name|withAutowireComponentPropertiesAllowPrivateSetter
parameter_list|(
name|boolean
name|autowireComponentPropertiesAllowPrivateSetter
parameter_list|)
block|{
name|this
operator|.
name|autowireComponentPropertiesAllowPrivateSetter
operator|=
name|autowireComponentPropertiesAllowPrivateSetter
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to use graceful hangup when Camel is stopping or when the JVM terminates.      */
DECL|method|withHangupInterceptorEnabled (boolean hangupInterceptorEnabled)
specifier|public
name|MainConfigurationProperties
name|withHangupInterceptorEnabled
parameter_list|(
name|boolean
name|hangupInterceptorEnabled
parameter_list|)
block|{
name|this
operator|.
name|hangupInterceptorEnabled
operator|=
name|hangupInterceptorEnabled
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the exit code for the application if duration was hit      */
DECL|method|withDurationHitExitCode (int durationHitExitCode)
specifier|public
name|MainConfigurationProperties
name|withDurationHitExitCode
parameter_list|(
name|int
name|durationHitExitCode
parameter_list|)
block|{
name|this
operator|.
name|durationHitExitCode
operator|=
name|durationHitExitCode
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets package names for scanning for {@link org.apache.camel.builder.RouteBuilder} classes as candidates to be included.      * If you are using Spring Boot then its instead recommended to use Spring Boots component scanning and annotate your route builder      * classes with `@Component`. In other words only use this for Camel Main in standalone mode.      */
DECL|method|withPackageScanRouteBuilders (String packageScanRouteBuilders)
specifier|public
name|MainConfigurationProperties
name|withPackageScanRouteBuilders
parameter_list|(
name|String
name|packageScanRouteBuilders
parameter_list|)
block|{
name|this
operator|.
name|packageScanRouteBuilders
operator|=
name|packageScanRouteBuilders
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

