begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.metrics
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|metrics
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|Map
import|;
end_import

begin_enum
DECL|enum|MetricsType
specifier|public
enum|enum
name|MetricsType
block|{
DECL|enumConstant|GAUGE
name|GAUGE
argument_list|(
literal|"gauge"
argument_list|)
block|,
DECL|enumConstant|COUNTER
name|COUNTER
argument_list|(
literal|"counter"
argument_list|)
block|,
DECL|enumConstant|HISTOGRAM
name|HISTOGRAM
argument_list|(
literal|"histogram"
argument_list|)
block|,
DECL|enumConstant|METER
name|METER
argument_list|(
literal|"meter"
argument_list|)
block|,
DECL|enumConstant|TIMER
name|TIMER
argument_list|(
literal|"timer"
argument_list|)
block|, ;
DECL|field|map
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|MetricsType
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MetricsType
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
static|static
block|{
for|for
control|(
name|MetricsType
name|type
range|:
name|EnumSet
operator|.
name|allOf
argument_list|(
name|MetricsType
operator|.
name|class
argument_list|)
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|type
operator|.
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|MetricsType (String name)
specifier|private
name|MetricsType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getByName (String name)
specifier|public
specifier|static
name|MetricsType
name|getByName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

