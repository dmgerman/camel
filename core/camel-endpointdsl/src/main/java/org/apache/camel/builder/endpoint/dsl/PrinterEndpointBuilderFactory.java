begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The printer component is used for sending messages to printers as print jobs.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|PrinterEndpointBuilderFactory
specifier|public
interface|interface
name|PrinterEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Printer component.      */
DECL|interface|PrinterEndpointBuilder
specifier|public
interface|interface
name|PrinterEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedPrinterEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedPrinterEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Number of copies to print.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|copies (int copies)
specifier|default
name|PrinterEndpointBuilder
name|copies
parameter_list|(
name|int
name|copies
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"copies"
argument_list|,
name|copies
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Number of copies to print.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|copies (String copies)
specifier|default
name|PrinterEndpointBuilder
name|copies
parameter_list|(
name|String
name|copies
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"copies"
argument_list|,
name|copies
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets DocFlavor to use.          *           * The option is a:<code>javax.print.DocFlavor</code> type.          *           * Group: producer          */
DECL|method|docFlavor (Object docFlavor)
specifier|default
name|PrinterEndpointBuilder
name|docFlavor
parameter_list|(
name|Object
name|docFlavor
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"docFlavor"
argument_list|,
name|docFlavor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets DocFlavor to use.          *           * The option will be converted to a<code>javax.print.DocFlavor</code>          * type.          *           * Group: producer          */
DECL|method|docFlavor (String docFlavor)
specifier|default
name|PrinterEndpointBuilder
name|docFlavor
parameter_list|(
name|String
name|docFlavor
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"docFlavor"
argument_list|,
name|docFlavor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets DocFlavor to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|flavor (String flavor)
specifier|default
name|PrinterEndpointBuilder
name|flavor
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"flavor"
argument_list|,
name|flavor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the stationary as defined by enumeration names in the          * javax.print.attribute.standard.MediaSizeName API. The default setting          * is to use North American Letter sized stationary. The value's case is          * ignored, e.g. values of iso_a4 and ISO_A4 may be used.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|mediaSize (String mediaSize)
specifier|default
name|PrinterEndpointBuilder
name|mediaSize
parameter_list|(
name|String
name|mediaSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mediaSize"
argument_list|,
name|mediaSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets MediaTray supported by the javax.print.DocFlavor API, for          * example upper,middle etc.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|mediaTray (String mediaTray)
specifier|default
name|PrinterEndpointBuilder
name|mediaTray
parameter_list|(
name|String
name|mediaTray
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mediaTray"
argument_list|,
name|mediaTray
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets mimeTypes supported by the javax.print.DocFlavor API.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|mimeType (String mimeType)
specifier|default
name|PrinterEndpointBuilder
name|mimeType
parameter_list|(
name|String
name|mimeType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mimeType"
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the page orientation.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|orientation (String orientation)
specifier|default
name|PrinterEndpointBuilder
name|orientation
parameter_list|(
name|String
name|orientation
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"orientation"
argument_list|,
name|orientation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the prefix name of the printer, it is useful when the printer          * name does not start with //hostname/printer.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|printerPrefix (String printerPrefix)
specifier|default
name|PrinterEndpointBuilder
name|printerPrefix
parameter_list|(
name|String
name|printerPrefix
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"printerPrefix"
argument_list|,
name|printerPrefix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * etting this option to false prevents sending of the print data to the          * printer.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|sendToPrinter (boolean sendToPrinter)
specifier|default
name|PrinterEndpointBuilder
name|sendToPrinter
parameter_list|(
name|boolean
name|sendToPrinter
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"sendToPrinter"
argument_list|,
name|sendToPrinter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * etting this option to false prevents sending of the print data to the          * printer.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|sendToPrinter (String sendToPrinter)
specifier|default
name|PrinterEndpointBuilder
name|sendToPrinter
parameter_list|(
name|String
name|sendToPrinter
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"sendToPrinter"
argument_list|,
name|sendToPrinter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets one sided or two sided printing based on the          * javax.print.attribute.standard.Sides API.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|sides (String sides)
specifier|default
name|PrinterEndpointBuilder
name|sides
parameter_list|(
name|String
name|sides
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"sides"
argument_list|,
name|sides
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Printer component.      */
DECL|interface|AdvancedPrinterEndpointBuilder
specifier|public
interface|interface
name|AdvancedPrinterEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|PrinterEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|PrinterEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedPrinterEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedPrinterEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedPrinterEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedPrinterEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Printer (camel-printer)      * The printer component is used for sending messages to printers as print      * jobs.      *       * Category: printing      * Available as of version: 2.1      * Maven coordinates: org.apache.camel:camel-printer      *       * Syntax:<code>lpr:hostname:port/printername</code>      *       * Path parameter: hostname (required)      * Hostname of the printer      *       * Path parameter: port      * Port number of the printer      *       * Path parameter: printername      * Name of the printer      */
DECL|method|printer (String path)
specifier|default
name|PrinterEndpointBuilder
name|printer
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|PrinterEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|PrinterEndpointBuilder
implements|,
name|AdvancedPrinterEndpointBuilder
block|{
specifier|public
name|PrinterEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"lpr"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|PrinterEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

