import React from 'react';
import PureRenderMixin from 'react-pure-render/mixin';

var NavigationItem = React.createClass({
  mixins: [PureRenderMixin],
  propTypes: {
    sectionName: React.PropTypes.string.isRequired,
    active: React.PropTypes.bool.isRequired,
    onClick: React.PropTypes.func.isRequired,
    href: React.PropTypes.string.isRequired
  },
  onClick() {
    this.props.onClick(this.props.sectionName);
  },
  render() {
    var {sectionName, href, active} = this.props;
    return (<a
      href={href}
      onClick={this.onClick}
      className={`line-height15 pad0x pad00y quiet block ${active ? 'fill-lighten0 round' : ''}`}>
      {sectionName}
    </a>);
  }
});

module.exports = NavigationItem;
